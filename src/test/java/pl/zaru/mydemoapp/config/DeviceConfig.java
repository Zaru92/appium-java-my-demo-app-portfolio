package pl.zaru.mydemoapp.config;

import java.util.Objects;
import java.util.Optional;

public record DeviceConfig(
    TargetType targetType,
    String deviceName,
    Optional<String> udid,
    Optional<String> platformVersion,
    Optional<Integer> systemPort,
    Optional<Integer> wdaLocalPort) {

  public DeviceConfig {
    Objects.requireNonNull(targetType, "targetType must not be null");

    deviceName = requireText(deviceName, "deviceName");

    systemPort = validatePort(systemPort, "systemPort");
    wdaLocalPort = validatePort(wdaLocalPort, "wdaLocalPort");

    if (systemPort.isPresent() && wdaLocalPort.isPresent()) {
      throw new IllegalArgumentException(
          "Only one platform-specific automation port may be configured.");
    }

    udid = normalize(udid, "udid");
    platformVersion = normalize(platformVersion, "platformVersion");

    if (targetType == TargetType.REAL_DEVICE && udid.isEmpty()) {
      throw new IllegalArgumentException("udid must be provided for a real device.");
    }
  }

  public DeviceConfig(
      TargetType targetType,
      String deviceName,
      Optional<String> udid,
      Optional<String> platformVersion) {

    this(targetType, deviceName, udid, platformVersion, Optional.empty(), Optional.empty());
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

  private static Optional<Integer> validatePort(Optional<Integer> value, String fieldName) {

    Optional<Integer> port = Objects.requireNonNull(value, fieldName + " must not be null");

    port.ifPresent(
        number -> {
          if (number < 1 || number > 65535) {
            throw new IllegalArgumentException(fieldName + " must be between 1 and 65535.");
          }
        });

    return port;
  }
}
