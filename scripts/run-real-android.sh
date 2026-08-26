#!/usr/bin/env bash

set -euo pipefail

readonly SCRIPT_DIRECTORY="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
readonly PROJECT_DIRECTORY="$(cd -- "${SCRIPT_DIRECTORY}/.." && pwd)"
readonly DEVICE_UDID="${ANDROID_REAL_UDID:?Set ANDROID_REAL_UDID before running this script.}"
readonly SYSTEM_PORT="${ANDROID_REAL_SYSTEM_PORT:-8201}"
readonly TEST_CLASS="${1:-AppLaunchSmokeTest}"

if ! command -v adb >/dev/null 2>&1; then
  echo "adb is not available on PATH." >&2
  exit 1
fi

device_state="$(adb -s "${DEVICE_UDID}" get-state 2>/dev/null || true)"

if [[ "${device_state}" != "device" ]]; then
  echo "Android device ${DEVICE_UDID} is not connected or authorized." >&2
  exit 1
fi

is_emulator="$(adb -s "${DEVICE_UDID}" shell getprop ro.kernel.qemu | tr -d '\r')"

if [[ "${is_emulator}" == "1" ]]; then
  echo "ANDROID_REAL_UDID points to an emulator, not a physical device." >&2
  exit 1
fi

device_name="$(adb -s "${DEVICE_UDID}" shell getprop ro.product.model | tr -d '\r')"
platform_version="$(
  adb -s "${DEVICE_UDID}" shell getprop ro.build.version.release | tr -d '\r'
)"

if [[ -z "${device_name}" || -z "${platform_version}" ]]; then
  echo "Could not read device name or Android version." >&2
  exit 1
fi

echo "Running ${TEST_CLASS} on ${device_name}, Android ${platform_version}."

cd "${PROJECT_DIRECTORY}"

exec ./mvnw clean \
  -Dplatform=android \
  -DtargetType=real \
  "-DdeviceName=${device_name}" \
  "-Dudid=${DEVICE_UDID}" \
  "-DplatformVersion=${platform_version}" \
  "-DsystemPort=${SYSTEM_PORT}" \
  "-Dtest=${TEST_CLASS}" \
  test
