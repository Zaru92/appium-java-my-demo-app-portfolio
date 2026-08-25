package pl.zaru.mydemoapp.config;

import java.util.Objects;
import java.util.Optional;

public record DeviceConfig(
    TargetType targetType,
    String deviceName,
    Optional<String> udid,
    Optional<String> platformVersion) {

  public DeviceConfig {
    Objects.requireNonNull(targetType, "targetType must not be null");

    deviceName = requireText(deviceName, "deviceName");
    udid = normalize(udid, "udid");
    platformVersion = normalize(platformVersion, "platformVersion");

    if (targetType == TargetType.REAL_DEVICE && udid.isEmpty()) {
      throw new IllegalArgumentException("udid must be provided for a real device.");
    }
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
