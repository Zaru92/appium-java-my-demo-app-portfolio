#!/usr/bin/env bash

set -uo pipefail

readonly SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
readonly PROJECT_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
readonly APPS_DIR="${PROJECT_ROOT}/src/test/resources/apps"
readonly ANDROID_APP="${APPS_DIR}/my-demo-app-android-2.2.0.apk"
readonly IOS_APP="${APPS_DIR}/my-demo-app-ios-simulator-2.2.2.zip"

failures=0
warnings=0
installed_drivers=""

pass() { printf '[PASS] %s\n' "$1"; }
warn() { printf '[WARN] %s\n' "$1"; warnings=$((warnings + 1)); }
fail() { printf '[FAIL] %s\n' "$1" >&2; failures=$((failures + 1)); }
section() { printf '\n%s\n' "$1"; }

usage() {
    printf 'Usage: %s [android|ios|all]\n' "$0" >&2
}

require_command() {
    if command -v "$1" >/dev/null 2>&1; then
        pass "Command available: $1"
    else
        fail "Required command is unavailable: $1"
    fi
}

check_driver() {
    local driver_name="$1"
    local output

    if ! printf '%s\n' "${installed_drivers}" | grep -q "${driver_name}@"; then
        fail "Appium driver is not installed: ${driver_name}"
        return
    fi

    pass "Appium driver is installed: ${driver_name}"

    if output="$(appium driver doctor "${driver_name}" 2>&1)"; then
        pass "Appium Doctor: ${driver_name} has no required fixes."
    else
        fail "Appium Doctor failed for ${driver_name}."
        printf '%s\n' "${output}" >&2
    fi
}

target="${1:-all}"

if (( $# > 1 )); then
    usage
    exit 2
fi

case "${target}" in
    android|ios|all) ;;
    *)
        usage
        exit 2
        ;;
esac

section "Core"
pass "Host: $(uname -s) $(uname -m)"

for command_name in java node npm appium curl shasum; do
    require_command "${command_name}"
done

if [[ -n "${JAVA_HOME:-}" && -x "${JAVA_HOME}/bin/java" ]]; then
    pass "JAVA_HOME: ${JAVA_HOME}"
else
    fail "JAVA_HOME is missing or invalid."
fi

if [[ -x "${PROJECT_ROOT}/mvnw" ]] \
    && "${PROJECT_ROOT}/mvnw" --quiet -DskipTests validate >/dev/null 2>&1; then
    pass "Maven Wrapper and JDK constraints are satisfied."
else
    fail "Maven validation failed; run ./mvnw validate for details."
fi

if command -v node >/dev/null 2>&1; then
    node_version="$(node --version)"

    if node -e 'const [major, minor] = process.versions.node.split(".").map(Number); process.exit((major === 20 && minor >= 19) || (major === 22 && minor >= 12) || major >= 24 ? 0 : 1);'; then
        pass "Node.js ${node_version} is supported by Appium 3."
    else
        fail "Node.js ${node_version} is not supported by Appium 3."
    fi
fi

if command -v npm >/dev/null 2>&1; then
    npm_version="$(npm --version)"
    npm_major="${npm_version%%.*}"

    if [[ "${npm_major}" =~ ^[0-9]+$ ]] && (( npm_major >= 10 )); then
        pass "npm ${npm_version} is supported by Appium 3."
    else
        fail "npm 10 or newer is required; found ${npm_version}."
    fi
fi

if command -v appium >/dev/null 2>&1; then
    appium_version="$(appium --version 2>/dev/null | sed -n '$p')"

    if [[ "${appium_version#v}" == 3.* ]]; then
        pass "Appium ${appium_version}"
    else
        fail "Appium 3.x is required; found ${appium_version:-unknown}."
    fi

    if ! installed_drivers="$(appium driver list --installed 2>&1)"; then
        fail "Appium could not list installed drivers."
    fi
fi

if [[ "${target}" == "android" || "${target}" == "all" ]]; then
    section "Android"

    if [[ -f "${ANDROID_APP}" ]]; then
        pass "Android application build is present."
    else
        fail "Android build is missing; run ./scripts/download-apps.sh android."
    fi

    android_sdk="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}"

    if [[ -n "${android_sdk}" && -d "${android_sdk}" ]]; then
        pass "Android SDK: ${android_sdk}"
    else
        fail "ANDROID_HOME or ANDROID_SDK_ROOT is missing or invalid."
    fi

    if [[ -n "${ANDROID_HOME:-}" && -n "${ANDROID_SDK_ROOT:-}" \
        && "${ANDROID_HOME}" != "${ANDROID_SDK_ROOT}" ]]; then
        warn "ANDROID_HOME and ANDROID_SDK_ROOT point to different directories."
    fi

    adb_command=""
    emulator_command=""

    if command -v adb >/dev/null 2>&1; then
        adb_command="$(command -v adb)"
    elif [[ -n "${android_sdk}" && -x "${android_sdk}/platform-tools/adb" ]]; then
        adb_command="${android_sdk}/platform-tools/adb"
        warn "ADB is installed but is not available on PATH."
    else
        fail "ADB could not be found."
    fi

    # Prefer the current SDK location over a potentially stale tools/emulator entry.
    if [[ -n "${android_sdk}" && -x "${android_sdk}/emulator/emulator" ]]; then
        emulator_command="${android_sdk}/emulator/emulator"
    elif command -v emulator >/dev/null 2>&1; then
        emulator_command="$(command -v emulator)"
    fi

    connected_target=0
    configured_avd=0

    if [[ -n "${adb_command}" ]] \
        && "${adb_command}" devices -l 2>/dev/null \
        | grep -Eq '^[^[:space:]]+[[:space:]]+device([[:space:]]|$)'; then
        connected_target=1
        pass "At least one online Android target is connected."
    else
        warn "No online Android target is currently connected."
    fi

    if [[ -n "${emulator_command}" ]] \
        && "${emulator_command}" -list-avds 2>/dev/null \
        | grep -q '[^[:space:]]'; then
        configured_avd=1
        pass "At least one Android Virtual Device is configured."
    else
        warn "No Android Virtual Device was found."
    fi

    if (( connected_target == 0 && configured_avd == 0 )); then
        fail "No usable Android execution target was found."
    fi

    check_driver uiautomator2
fi

if [[ "${target}" == "ios" || "${target}" == "all" ]]; then
    section "iOS"

    if [[ "$(uname -s)" != "Darwin" ]]; then
        fail "Local iOS execution requires macOS."
    else
        if [[ -f "${IOS_APP}" ]]; then
            pass "iOS Simulator application build is present."
        else
            fail "iOS build is missing; run ./scripts/download-apps.sh ios."
        fi

        for command_name in xcode-select xcodebuild xcrun ffmpeg; do
            require_command "${command_name}"
        done

        if xcode_path="$(xcode-select -p 2>/dev/null)"; then
            pass "Xcode developer directory: ${xcode_path}"
        else
            fail "Xcode Command Line Tools are not configured."
        fi

        if command -v xcrun >/dev/null 2>&1 \
            && xcrun simctl list devices available 2>/dev/null \
            | grep -Eq '\((Booted|Shutdown)\)'; then
            pass "At least one iOS Simulator device is available."
        else
            fail "No available iOS Simulator device was found."
        fi

        check_driver xcuitest
    fi
fi

section "Summary"
printf 'Failures: %d\n' "${failures}"
printf 'Warnings: %d\n' "${warnings}"

if (( failures > 0 )); then
    exit 1
fi

pass "Environment is ready for target: ${target}."
