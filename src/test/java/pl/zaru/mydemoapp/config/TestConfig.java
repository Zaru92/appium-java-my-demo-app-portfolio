package pl.zaru.mydemoapp.config;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

public record TestConfig(
    URI appiumUrl,
    MobilePlatform platform,
    String deviceName,
    Optional<String> udid,
    Optional<String> platformVersion,
    Optional<String> appWaitActivity,
    Path appPath,
    Duration newCommandTimeout) {

  public TestConfig {
    Objects.requireNonNull(appiumUrl, "appiumUrl must not be null");
    Objects.requireNonNull(platform, "platform must not be null");
    Objects.requireNonNull(appPath, "appPath must not be null");
    Objects.requireNonNull(newCommandTimeout, "newCommandTimeout must not be null");

    deviceName = requireText(deviceName, "deviceName");
    udid = normalize(udid, "udid");
    platformVersion = normalize(platformVersion, "platformVersion");
    appWaitActivity = normalize(appWaitActivity, "appWaitActivity");
    appPath = appPath.toAbsolutePath().normalize();

    if (!appiumUrl.isAbsolute() || appiumUrl.getHost() == null) {
      throw new IllegalArgumentException("appiumUrl must be an absolute URL.");
    }

    if (newCommandTimeout.isZero() || newCommandTimeout.isNegative()) {
      throw new IllegalArgumentException("newCommandTimeout must be positive.");
    }
  }

  public String automationName() {
    return platform.automationName();
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank.");
    }

    return value.trim();
  }

  private static Optional<String> normalize(Optional<String> value, String fieldName) {
    return Objects.requireNonNull(value, fieldName + " must not be null")
        .map(String::trim)
        .filter(text -> !text.isEmpty());
  }
}
