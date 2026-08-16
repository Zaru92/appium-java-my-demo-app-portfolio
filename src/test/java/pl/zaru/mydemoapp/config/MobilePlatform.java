package pl.zaru.mydemoapp.config;

import java.util.Locale;

public enum MobilePlatform {
  ANDROID("android", "UiAutomator2", "android.properties"),
  IOS("ios", "XCUITest", "ios.properties");

  private final String value;
  private final String automationName;
  private final String configFile;

  MobilePlatform(String value, String automationName, String configFile) {
    this.value = value;
    this.automationName = automationName;
    this.configFile = configFile;
  }

  public String value() {
    return value;
  }

  public String automationName() {
    return automationName;
  }

  String configFile() {
    return configFile;
  }

  public static MobilePlatform from(String value) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("Platform must not be blank.");
    }

    return switch (value.trim().toLowerCase(Locale.ROOT)) {
      case "android" -> ANDROID;
      case "ios" -> IOS;
      default ->
          throw new IllegalArgumentException(
              "Unsupported platform: " + value + ". Expected android or ios.");
    };
  }
}
