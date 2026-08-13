#!/usr/bin/env bash

set -Eeuo pipefail

readonly SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
readonly PROJECT_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
readonly APPS_DIR="${PROJECT_ROOT}/src/test/resources/apps"

readonly ANDROID_FILE="my-demo-app-android-2.2.0.apk"
readonly ANDROID_URL="https://github.com/saucelabs/my-demo-app-android/releases/download/2.2.0/mda-2.2.0-25.apk"
readonly ANDROID_SHA256="318ef64bdcaff18e576d962ab1f557e0a2683b9b5210a6bb6b25cb0caeef62b4"

readonly IOS_FILE="my-demo-app-ios-simulator-2.2.2.zip"
readonly IOS_URL="https://github.com/saucelabs/my-demo-app-ios/releases/download/2.2.2/SauceLabs-Demo-App.Simulator.zip"
readonly IOS_SHA256="96b08d5ac74dd817d95fbd8332ae9385bb076af38d56d13d8465345cb1797139"

temporary_file=""

cleanup() {
    if [[ -n "${temporary_file}" && -f "${temporary_file}" ]]; then
        rm -f "${temporary_file}"
    fi
}

trap cleanup EXIT

require_command() {
    local command_name="$1"

    if ! command -v "${command_name}" >/dev/null 2>&1; then
        printf 'ERROR: Required command not found: %s\n' "${command_name}" >&2
        exit 1
    fi
}

calculate_sha256() {
    local checksum_output

    checksum_output="$(shasum -a 256 "$1")"
    printf '%s\n' "${checksum_output%% *}"
}

download_asset() {
    local file_name="$1"
    local download_url="$2"
    local expected_checksum="$3"
    local destination="${APPS_DIR}/${file_name}"
    local actual_checksum

    if [[ -f "${destination}" ]]; then
        actual_checksum="$(calculate_sha256 "${destination}")"

        if [[ "${actual_checksum}" == "${expected_checksum}" ]]; then
            printf '[OK] %s is already present and verified.\n' "${file_name}"
            return
        fi

        printf '[WARN] %s has an invalid checksum and will be replaced.\n' "${file_name}"
    fi

    temporary_file="$(mktemp "${destination}.tmp.XXXXXX")"

    printf '[DOWNLOAD] %s\n' "${file_name}"

    curl \
        --fail \
        --location \
        --silent \
        --show-error \
        --retry 3 \
        --retry-delay 2 \
        --output "${temporary_file}" \
        "${download_url}"

    actual_checksum="$(calculate_sha256 "${temporary_file}")"

    if [[ "${actual_checksum}" != "${expected_checksum}" ]]; then
        printf 'ERROR: Checksum verification failed for %s.\n' "${file_name}" >&2
        printf 'Expected: %s\n' "${expected_checksum}" >&2
        printf 'Actual:   %s\n' "${actual_checksum}" >&2
        exit 1
    fi

    mv "${temporary_file}" "${destination}"
    temporary_file=""

    printf '[OK] %s downloaded and verified.\n' "${file_name}"
}

main() {
    local target="${1:-all}"

    if (( $# > 1 )); then
        printf 'Usage: %s [android|ios|all]\n' "$0" >&2
        exit 2
    fi

    require_command curl
    require_command mktemp
    require_command shasum

    mkdir -p "${APPS_DIR}"

    case "${target}" in
        android)
            download_asset "${ANDROID_FILE}" "${ANDROID_URL}" "${ANDROID_SHA256}"
            ;;
        ios)
            download_asset "${IOS_FILE}" "${IOS_URL}" "${IOS_SHA256}"
            ;;
        all)
            download_asset "${ANDROID_FILE}" "${ANDROID_URL}" "${ANDROID_SHA256}"
            download_asset "${IOS_FILE}" "${IOS_URL}" "${IOS_SHA256}"
            ;;
        *)
            printf 'Usage: %s [android|ios|all]\n' "$0" >&2
            exit 2
            ;;
    esac
}

main "$@"
