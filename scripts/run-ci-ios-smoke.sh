#!/usr/bin/env bash

set -euo pipefail

readonly SCRIPT_DIRECTORY="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
readonly PROJECT_DIRECTORY="$(cd "${SCRIPT_DIRECTORY}/.." && pwd)"
readonly WDA_LOCAL_PORT="${IOS_CI_WDA_LOCAL_PORT:-8100}"
readonly TEST_CLASS="${1:-AppLaunchSmokeTest}"
readonly APPIUM_LOG="${PROJECT_DIRECTORY}/target/appium-server.log"

appium_pid=""
simulator_udid=""
simulator_started_by_script=false

cleanup() {
  if [[ -n "${appium_pid}" ]] && kill -0 "${appium_pid}" 2>/dev/null; then
    kill "${appium_pid}" 2>/dev/null || true
    wait "${appium_pid}" 2>/dev/null || true
  fi

  if [[ "${simulator_started_by_script}" == true && -n "${simulator_udid}" ]]; then
    xcrun simctl shutdown "${simulator_udid}" >/dev/null 2>&1 || true
  fi
}

select_simulator() {
  xcrun simctl list devices available --json | python3 -c '
import json
import re
import sys

inventory = json.load(sys.stdin)
candidates = []

for runtime_identifier, devices in inventory.get("devices", {}).items():
    match = re.search(r"SimRuntime\.iOS-(\d+(?:-\d+)*)$", runtime_identifier)

    if match is None:
        continue

    platform_version = match.group(1).replace("-", ".")
    version_key = tuple(int(part) for part in platform_version.split("."))

    for device in devices:
        name = device.get("name", "")

        if not device.get("isAvailable", True) or not name.startswith("iPhone"):
            continue

        if name.endswith(" Pro"):
            model_priority = 3
        elif name.endswith(" Pro Max"):
            model_priority = 2
        else:
            model_priority = 1

        candidates.append(
            (
                version_key,
                model_priority,
                name,
                device["udid"],
                platform_version,
                device.get("state", "Shutdown"),
            )
        )

if not candidates:
    raise SystemExit("No available iPhone simulator was found.")

_, _, name, udid, platform_version, state = max(candidates)
print("\t".join((name, udid, platform_version, state)))
'
}

trap cleanup EXIT

for command_name in appium curl python3 xcodebuild xcrun; do
  if ! command -v "${command_name}" >/dev/null 2>&1; then
    echo "${command_name} is not available on PATH." >&2
    exit 1
  fi
done

mkdir -p "${PROJECT_DIRECTORY}/target"
cd "${PROJECT_DIRECTORY}"

xcodebuild -version
echo "Available iOS simulators:"
xcrun simctl list devices available

simulator_record="$(select_simulator)"
IFS=$'\t' read -r simulator_name simulator_udid platform_version simulator_state \
  <<<"${simulator_record}"

echo "Selected ${simulator_name}, iOS ${platform_version}, UDID ${simulator_udid}."

if [[ "${simulator_state}" != "Booted" ]]; then
  xcrun simctl boot "${simulator_udid}"
  simulator_started_by_script=true
fi

xcrun simctl bootstatus "${simulator_udid}" -b

appium >"${APPIUM_LOG}" 2>&1 &
appium_pid=$!

for attempt in {1..60}; do
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
  echo "Appium server did not become ready within 60 seconds." >&2
  exit 1
fi

./mvnw --batch-mode --no-transfer-progress \
  -Dplatform=ios \
  -DtargetType=simulator \
  "-DdeviceName=${simulator_name}" \
  "-Dudid=${simulator_udid}" \
  "-DplatformVersion=${platform_version}" \
  "-DwdaLocalPort=${WDA_LOCAL_PORT}" \
  "-Dtest=${TEST_CLASS}" \
  test
