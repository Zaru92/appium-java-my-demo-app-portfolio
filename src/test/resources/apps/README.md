# Application Builds

Application binaries are downloaded on demand and intentionally excluded from
version control.

| Target | Upstream version | Local file |
|---|---:|---|
| Android emulator or physical device | 2.2.0 | `my-demo-app-android-2.2.0.apk` |
| iOS Simulator | 2.2.2 | `my-demo-app-ios-simulator-2.2.2.zip` |

## Download

Download and verify both applications:

```bash
./scripts/download-apps.sh
```

Download one platform only:

```bash
./scripts/download-apps.sh android
./scripts/download-apps.sh ios
```

The script verifies every build using a pinned SHA-256 checksum.

The iOS ZIP contains an `.app` compiled for iOS Simulator runtimes. It cannot
be installed on a physical iPhone. Physical-device execution requires a
correctly signed `.ipa` and will use a separate device profile.
