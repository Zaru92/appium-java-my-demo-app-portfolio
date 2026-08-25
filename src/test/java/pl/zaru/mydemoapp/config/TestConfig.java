package pl.zaru.mydemoapp.config;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

public record TestConfig(
    URI appiumUrl,
    Platform platform,
    DeviceConfig device,
    Optional<String> appWaitActivity,
    Path appPath,
    Duration newCommandTimeout) {

  public TestConfig {
    Objects.requireNonNull(appiumUrl, "appiumUrl must not be null");
    Objects.requireNonNull(platform, "platform must not be null");
    Objects.requireNonNull(device, "device must not be null");
    Objects.requireNonNull(appPath, "appPath must not be null");
    Objects.requireNonNull(newCommandTimeout, "newCommandTimeout must not be null");

    appWaitActivity = normalize(appWaitActivity, "appWaitActivity");
    appPath = appPath.toAbsolutePath().normalize();

    if (!appiumUrl.isAbsolute() || appiumUrl.getHost() == null) {
      throw new IllegalArgumentException("appiumUrl must be an absolute URL.");
    }

    if (!device.targetType().supports(platform)) {
      throw new IllegalArgumentException(
          "Target type %s is not supported for platform %s."
              .formatted(device.targetType().value(), platform.value()));
    }

    if (newCommandTimeout.isZero() || newCommandTimeout.isNegative()) {
      throw new IllegalArgumentException("newCommandTimeout must be positive.");
    }

    if (platform == Platform.ANDROID && device.wdaLocalPort().isPresent()) {
      throw new IllegalArgumentException("wdaLocalPort is supported only for iOS.");
    }

    if (platform == Platform.IOS && device.systemPort().isPresent()) {
      throw new IllegalArgumentException("systemPort is supported only for Android.");
    }
  }

  public String automationName() {
    return platform.automationName();
  }

  private static Optional<String> normalize(Optional<String> value, String fieldName) {
    return Objects.requireNonNull(value, fieldName + " must not be null")
        .map(String::trim)
        .filter(text -> !text.isEmpty());
  }
}
