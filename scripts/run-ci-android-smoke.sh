#!/usr/bin/env bash

set -euo pipefail

readonly SCRIPT_DIRECTORY="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
readonly PROJECT_DIRECTORY="$(cd -- "${SCRIPT_DIRECTORY}/.." && pwd)"
readonly DEVICE_UDID="${ANDROID_SERIAL:-emulator-5554}"
readonly SYSTEM_PORT="${ANDROID_CI_SYSTEM_PORT:-8200}"
readonly TEST_CLASS="${1:-AppLaunchSmokeTest}"
readonly APPIUM_LOG="${PROJECT_DIRECTORY}/target/appium-server.log"

appium_pid=""

cleanup() {
  if [[ -n "${appium_pid}" ]] && kill -0 "${appium_pid}" 2>/dev/null; then
    kill "${appium_pid}" 2>/dev/null || true
    wait "${appium_pid}" 2>/dev/null || true
  fi
}

trap cleanup EXIT

for command_name in appium curl adb; do
  if ! command -v "${command_name}" >/dev/null 2>&1; then
    echo "${command_name} is not available on PATH." >&2
    exit 1
  fi
done

if [[ "$(adb -s "${DEVICE_UDID}" get-state 2>/dev/null || true)" != "device" ]]; then
  echo "Android emulator ${DEVICE_UDID} is not available." >&2
  exit 1
fi

mkdir -p "${PROJECT_DIRECTORY}/target"

cd "${PROJECT_DIRECTORY}"

appium >"${APPIUM_LOG}" 2>&1 &
appium_pid=$!

for attempt in {1..30}; do
  if curl --fail --silent http://127.0.0.1:4723/status >/dev/null; then
    break
  fi

  if ! kill -0 "${appium_pid}" 2>/dev/null; then
    cat "${APPIUM_LOG}" >&2
    exit 1
  fi

  sleep 1
done

if ! curl --fail --silent http://127.0.0.1:4723/status >/dev/null; then
  cat "${APPIUM_LOG}" >&2
  echo "Appium server did not become ready within 30 seconds." >&2
  exit 1
fi

./mvnw --batch-mode --no-transfer-progress \
  -Dplatform=android \
  "-DdeviceName=Android CI Emulator" \
  "-Dudid=${DEVICE_UDID}" \
  "-DsystemPort=${SYSTEM_PORT}" \
  "-Dtest=${TEST_CLASS}" \
  test
