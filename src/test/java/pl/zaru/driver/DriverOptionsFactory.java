package pl.zaru.driver;

import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.util.Objects;
import org.openqa.selenium.Capabilities;
import pl.zaru.config.TestConfig;

final class DriverOptionsFactory {
  private DriverOptionsFactory() {}

  static Capabilities create(TestConfig config) {
    Objects.requireNonNull(config, "config must not be null");

    return switch (config.platform()) {
      case ANDROID -> createAndroidOptions(config);
      case IOS -> createIosOptions(config);
    };
  }

  private static UiAutomator2Options createAndroidOptions(TestConfig config) {
    UiAutomator2Options options =
        new UiAutomator2Options()
            .setDeviceName(config.deviceName())
            .setApp(config.appPath().toString())
            .setNewCommandTimeout(config.newCommandTimeout());

    config.udid().ifPresent(options::setUdid);
    config.platformVersion().ifPresent(options::setPlatformVersion);
    config.appWaitActivity().ifPresent(options::setAppWaitActivity);

    return options;
  }

  private static XCUITestOptions createIosOptions(TestConfig config) {
    XCUITestOptions options =
        new XCUITestOptions()
            .setDeviceName(config.deviceName())
            .setApp(config.appPath().toString())
            .setNewCommandTimeout(config.newCommandTimeout())
            .setConnectHardwareKeyboard(true)
            .setForceSimulatorSoftwareKeyboardPresence(false);

    config.udid().ifPresent(options::setUdid);
    config.platformVersion().ifPresent(options::setPlatformVersion);

    return options;
  }
}
