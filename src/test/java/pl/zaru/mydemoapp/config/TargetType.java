package pl.zaru.mydemoapp.config;

import java.util.Locale;
import java.util.Objects;

public enum TargetType {
  EMULATOR("emulator"),
  SIMULATOR("simulator"),
  REAL_DEVICE("real");

  private final String value;

  TargetType(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }

  public boolean supports(Platform platform) {
    Objects.requireNonNull(platform, "platform must not be null");

    return switch (platform) {
      case ANDROID -> this == EMULATOR || this == REAL_DEVICE;
      case IOS -> this == SIMULATOR || this == REAL_DEVICE;
    };
  }

  public static TargetType from(String value) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("Target type must not be blank.");
    }

    return switch (value.trim().toLowerCase(Locale.ROOT)) {
      case "emulator" -> EMULATOR;
      case "simulator" -> SIMULATOR;
      case "real", "real-device", "real_device" -> REAL_DEVICE;
      default ->
          throw new IllegalArgumentException(
              "Unsupported target type: " + value + ". Expected emulator, simulator, or real.");
    };
  }
}
