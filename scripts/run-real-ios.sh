#!/usr/bin/env bash

set -euo pipefail

readonly SCRIPT_DIRECTORY="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
readonly PROJECT_DIRECTORY="$(cd -- "${SCRIPT_DIRECTORY}/.." && pwd)"
readonly DEVICE_UDID="${IOS_REAL_UDID:?Set IOS_REAL_UDID before running this script.}"
readonly WDA_LOCAL_PORT="${IOS_REAL_WDA_LOCAL_PORT:-8101}"
readonly APP_PATH="${IOS_REAL_APP_PATH:-src/test/resources/apps/local/my-demo-app-ios-real-2.2.2.ipa}"
readonly TUNNEL_REGISTRY_URL="${IOS_REAL_TUNNEL_REGISTRY_URL:-http://127.0.0.1:42314/remotexpc/tunnels}"
readonly TEST_CLASS="${1:-AppLaunchSmokeTest}"

if ! command -v xcrun >/dev/null 2>&1; then
  echo "xcrun is not available on PATH." >&2
  exit 1
fi

if ! command -v curl >/dev/null 2>&1; then
  echo "curl is not available on PATH." >&2
  exit 1
fi

if [[ ! "${WDA_LOCAL_PORT}" =~ ^[0-9]+$ ]] \
  || ((WDA_LOCAL_PORT < 1 || WDA_LOCAL_PORT > 65535)); then
  echo "IOS_REAL_WDA_LOCAL_PORT must be between 1 and 65535." >&2
  exit 1
fi

if [[ "${APP_PATH}" = /* ]]; then
  application_path="${APP_PATH}"
else
  application_path="${PROJECT_DIRECTORY}/${APP_PATH}"
fi

if [[ ! -f "${application_path}" ]]; then
  echo "iOS application does not exist: ${application_path}" >&2
  exit 1
fi

if [[ "${application_path}" != *.ipa ]]; then
  echo "A real iOS device requires an .ipa application: ${application_path}" >&2
  exit 1
fi

device_line="$(
  xcrun xctrace list devices 2>/dev/null \
    | awk -v udid="${DEVICE_UDID}" 'index($0, "(" udid ")") { print; exit }'
)"

if [[ -z "${device_line}" ]]; then
  echo "Real iOS device ${DEVICE_UDID} is not visible to Xcode." >&2
  exit 1
fi

device_info="${device_line% (${DEVICE_UDID})}"
platform_version="${device_info##* (}"
platform_version="${platform_version%)}"
device_name="${device_info% (${platform_version})}"

if [[ -z "${device_name}" || -z "${platform_version}" ]]; then
  echo "Could not read device name or iOS version." >&2
  exit 1
fi

tunnel_registry_response="$(
  curl --fail --silent --show-error --max-time 5 "${TUNNEL_REGISTRY_URL}" || true
)"

if [[ -z "${tunnel_registry_response}" ]]; then
  echo "Remote XPC tunnel registry is not reachable at ${TUNNEL_REGISTRY_URL}." >&2
  exit 1
fi

if [[ "${tunnel_registry_response}" != *"\"${DEVICE_UDID}\":"* ]]; then
  echo "No active Remote XPC tunnel found for device ${DEVICE_UDID}." >&2
  exit 1
fi

echo "Running ${TEST_CLASS} on ${device_name}, iOS ${platform_version}."

cd "${PROJECT_DIRECTORY}"

exec ./mvnw clean \
  -Dplatform=ios \
  -DtargetType=real \
  "-DdeviceName=${device_name}" \
  "-Dudid=${DEVICE_UDID}" \
  "-DplatformVersion=${platform_version}" \
  "-Dapp=${application_path}" \
  "-DwdaLocalPort=${WDA_LOCAL_PORT}" \
  "-Dtest=${TEST_CLASS}" \
  test
