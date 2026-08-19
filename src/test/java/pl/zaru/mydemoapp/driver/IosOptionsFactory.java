package pl.zaru.mydemoapp.driver;

import io.appium.java_client.ios.options.XCUITestOptions;
import pl.zaru.mydemoapp.config.TestConfig;

final class IosOptionsFactory {

  private IosOptionsFactory() {}

  static XCUITestOptions create(TestConfig config) {
    XCUITestOptions options =
        new XCUITestOptions()
            .setDeviceName(config.deviceName())
            .setApp(config.appPath().toString())
            .setNewCommandTimeout(config.newCommandTimeout())
            .setConnectHardwareKeyboard(true)
            .setForceSimulatorSoftwareKeyboardPresence(false);

    options.setCapability("appium:connectHardwareKeyboard", true);

    config.udid().ifPresent(options::setUdid);

    config.platformVersion().ifPresent(options::setPlatformVersion);

    return options;
  }
}
