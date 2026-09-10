# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this
project follows [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.0] - 2026-09-10

### Added

- Cross-platform Appium test automation framework for Android and iOS, built with Java 21,
  TestNG, and Maven.
- Shared business scenarios with platform-specific Page Objects for catalog, cart,
  authentication, checkout, and WebView navigation.
- Support for Android emulators and physical devices, iOS Simulators and physical devices,
  plus parallel Android/iOS smoke execution.
- Environment preflight checks for Appium, application binaries, devices, automation ports,
  and iOS Remote XPC tunnels.
- Layered runtime configuration with property-file defaults, TestNG suite parameters, and Maven
  system-property overrides.
- Allure reporting with business steps, environment metadata, screenshots, and page source
  captured on failure.
- Reproducible setup and execution scripts, including verified application downloads.
- GitHub Actions workflows for framework verification, Android and iOS smoke tests, CodeQL,
  dependency review, and Dependabot updates.

### Changed

- Separated device-independent framework tests from mobile integration tests using the
  `*Test` and `*IT` naming conventions.
- Migrated Maven Surefire and Failsafe to 3.6.0 with TestNG execution through JUnit Platform.
- Hardened driver lifecycle, configuration isolation, waits, context switching, iOS keyboard
  handling, system command execution, and parallel-run validation.

### Fixed

- Ensured failure artifacts are attached before the Allure test lifecycle is finalized.
- Forwarded supported Maven `-D` overrides to the native parallel TestNG runner.

### Known limitations

- The iOS WebView scenario remains skipped because the upstream application's WKWebView is not
  exposed as inspectable. See [Known application issues](docs/known-issues.md).
- Application binaries are intentionally excluded from version control and must be downloaded
  with the provided setup script.

[Unreleased]: https://github.com/Zaru92/appium-java-my-demo-app-portfolio/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/Zaru92/appium-java-my-demo-app-portfolio/releases/tag/v1.0.0
