# Appium Java My Demo App Portfolio

[![Framework CI](https://github.com/Zaru92/appium-java-my-demo-app-portfolio/actions/workflows/framework-ci.yml/badge.svg)](https://github.com/Zaru92/appium-java-my-demo-app-portfolio/actions/workflows/framework-ci.yml)

Cross-platform mobile UI automation framework for
[Sauce Labs My Demo App](https://github.com/saucelabs/my-demo-app-android), built with Java,
Appium, Maven, and TestNG.

The same test scenarios run on Android and iOS through shared page contracts, while
platform-specific page objects contain native locators and interactions. The project supports
emulators, simulators, physical devices, parallel execution, environment preflight checks, and
Allure reporting.

## Highlights

- One cross-platform test layer for Android and iOS
- Platform-specific Page Objects selected by `ScreenFactory`
- Android emulator and physical-device execution
- iOS Simulator and physical-device execution
- Thread-safe Appium driver management with `ThreadLocal`
- Layered configuration with command-line and TestNG suite overrides
- Preflight validation of the Appium server, application binary, devices, ports, and Remote XPC
  tunnels
- Parallel Android and iOS smoke execution with isolated automation ports
- Allure environment metadata, screenshots, and page source captured on failure
- Device-independent framework verification in GitHub Actions
- Reproducible Maven Wrapper build and automated Java formatting checks

## Technology

| Component | Version or role |
|---|---|
| Java | 21 language and bytecode baseline |
| Maven Wrapper | 3.9.16 |
| Appium Java Client | 10.1.1 |
| Selenium | 4.43.0 |
| TestNG | 7.12.0 |
| Allure TestNG | 2.35.4 |
| SLF4J / Logback | Structured runtime logging |
| Spotless / google-java-format | Source formatting verification |
| GitHub Actions | Device-independent CI quality gate |

## Test coverage

| Area | Automated scenarios |
|---|---|
| Catalog | Application launch and product details |
| Cart | Add a product, update quantity, and remove a product |
| Authentication | Require login before checkout and continue after valid login |
| Checkout | Shipping address, payment details, order review, and order confirmation |

Tests are assigned to functional groups including `smoke`, `regression`, `e2e`, `catalog`,
`cart`, `authentication`, and `checkout`.

## Architecture

| Package | Responsibility |
|---|---|
| `base` | Test lifecycle and Appium session setup/cleanup |
| `config` | Platform, device, and runtime configuration |
| `device` | Environment and execution-target preflight checks |
| `driver` | Driver lifecycle, capabilities, and platform driver creation |
| `pages.contracts` | Shared cross-platform screen APIs |
| `pages.android` | Android Page Objects and locators |
| `pages.ios` | iOS Page Objects and locators |
| `actions` | Platform-specific reusable mobile interactions |
| `testdata` | Immutable test models and factories |
| `listeners` | Suite preflight and failure handling |
| `reporting` | Allure environment data and diagnostic artifacts |
| `tests` | Business-level mobile test scenarios |

## Supported execution targets

| Platform | Local virtual target | Physical target |
|---|---|---|
| Android | Android Emulator with UiAutomator2 | USB-connected device through ADB |
| iOS | iOS Simulator with XCUITest | Signed IPA, WebDriverAgent, and Remote XPC tunnel |

Physical iOS execution currently targets iOS 18 or newer because it relies on the Remote XPC
tunnel infrastructure used by the XCUITest driver.

## Prerequisites

Core requirements:

- JDK 21 or newer
- Node.js supported by Appium 3
- npm 10 or newer
- Appium 3.x
- Maven Wrapper included in the repository

Android execution additionally requires:

- Android SDK
- ADB and an emulator or authorized physical device
- Appium `uiautomator2` driver

iOS execution additionally requires:

- macOS and Xcode
- An available iOS Simulator or trusted physical device
- Appium `xcuitest` driver
- `ffmpeg`
- A correctly signed IPA and WebDriverAgent for a physical device
- An active Remote XPC tunnel for iOS 18+

Install the Appium drivers when needed:

```bash
appium driver install uiautomator2
appium driver install xcuitest
```

## Application builds

Application binaries are intentionally excluded from version control.

Download and verify the public Android and iOS Simulator builds:

```bash
./scripts/download-apps.sh
```

Download one platform only:

```bash
./scripts/download-apps.sh android
./scripts/download-apps.sh ios
```

The download script verifies each binary against a pinned SHA-256 checksum. A physical iOS build
must be signed locally and stored outside version control, by default at:

```text
src/test/resources/apps/local/my-demo-app-ios-real-2.2.2.ipa
```

## Environment verification

Verify the complete local environment:

```bash
./scripts/verify-environment.sh all
```

Verify one platform only:

```bash
./scripts/verify-environment.sh android
./scripts/verify-environment.sh ios
```

Start the Appium server before running mobile tests:

```bash
appium
```

## Running tests

### Android Emulator

```bash
./mvnw -Dplatform=android -Dtest=AppLaunchSmokeTest test
```

### iOS Simulator

```bash
./mvnw -Dplatform=ios -Dtest=AppLaunchSmokeTest test
```

### TestNG group

```bash
./mvnw -Dplatform=android -Dgroups=smoke test
./mvnw -Dplatform=ios -Dgroups=regression test
```

### Parallel Android and iOS smoke suite

Update the device identifiers in `src/test/resources/suites/testng-parallel.xml`, start both
targets, and run:

```bash
./mvnw clean \
  -Dsurefire.suiteXmlFiles=src/test/resources/suites/testng-parallel.xml \
  test
```

The suite validates that every parallel target has a unique UDID and platform-specific automation
port (`systemPort` for Android or `wdaLocalPort` for iOS).

### Physical Android device

```bash
export ANDROID_REAL_UDID=<android-device-udid>
./scripts/run-real-android.sh
```

Optional overrides:

```bash
export ANDROID_REAL_SYSTEM_PORT=8201
./scripts/run-real-android.sh ProductDetailsTest
```

The runner verifies that the UDID belongs to an authorized physical device and automatically
reads its model and Android version.

### Physical iOS device

Create and keep the Remote XPC tunnel running in a separate terminal:

```bash
sudo appium driver run xcuitest tunnel-creation -- --udid <ios-device-udid>
```

Then run the test:

```bash
export IOS_REAL_UDID=<ios-device-udid>
./scripts/run-real-ios.sh
```

Optional overrides:

```bash
export IOS_REAL_APP_PATH=/absolute/path/to/application.ipa
export IOS_REAL_WDA_LOCAL_PORT=8101
export IOS_REAL_TUNNEL_REGISTRY_URL=http://127.0.0.1:42314/remotexpc/tunnels
./scripts/run-real-ios.sh ProductDetailsTest
```

The runner verifies the IPA, retrieves the device name and iOS version from Xcode, and confirms
that the configured UDID has an active Remote XPC tunnel.

## Configuration

Default configuration is stored in:

- `src/test/resources/config/common.properties`
- `src/test/resources/config/android.properties`
- `src/test/resources/config/ios.properties`

Runtime system properties have the highest priority, followed by TestNG suite parameters and the
platform property files. Supported overrides include:

```text
platform
targetType
appium.url
deviceName
udid
platformVersion
app
newCommandTimeoutSeconds
appWaitActivity
systemPort
wdaLocalPort
```

Example:

```bash
./mvnw \
  -Dplatform=android \
  -DtargetType=real \
  -Dudid=<android-device-udid> \
  -DdeviceName="Pixel 7" \
  -DsystemPort=8201 \
  -Dtest=AppLaunchSmokeTest \
  test
```

## Framework verification

Run all device-independent configuration and infrastructure tests together with the formatting
quality gate:

```bash
./mvnw --batch-mode --no-transfer-progress \
  -Dsurefire.suiteXmlFiles=src/test/resources/suites/testng-framework.xml \
  clean verify
```

This command does not start Appium or create a mobile session. The same verification runs in
GitHub Actions for pushes and pull requests targeting `main`.

## Reports and diagnostics

Allure results are written to:

```text
target/allure-results
```

Generate and open the report:

```bash
./mvnw allure:serve
```

For failed mobile tests, the listener stores and attaches:

- a PNG screenshot,
- the current Appium page source in XML format.

Local copies are written to:

```text
target/failure-artifacts
```

The Allure environment file records the platform, target type, device, automation engine,
application, Appium server, Java version, host OS, and automation port.

## Continuous integration

`Framework CI` runs on Ubuntu for pushes and pull requests targeting `main`. It uses JDK 21 and
the Maven Wrapper to:

1. compile all framework and test sources,
2. run the device-independent TestNG suite,
3. verify Java formatting with Spotless,
4. upload Surefire and Allure results as workflow artifacts.

Mobile E2E tests remain local because GitHub-hosted runners do not provide the configured physical
devices or the project application binaries. They can later be moved to a self-hosted runner or a
cloud device provider.
