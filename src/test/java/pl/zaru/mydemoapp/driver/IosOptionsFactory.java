package pl.zaru.mydemoapp.driver;

import io.appium.java_client.ios.options.XCUITestOptions;
import java.time.Duration;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.TargetType;
import pl.zaru.mydemoapp.config.TestConfig;

final class IosOptionsFactory {

  private static final Duration WDA_LAUNCH_TIMEOUT = Duration.ofMinutes(5);

  private IosOptionsFactory() {}

  static XCUITestOptions create(TestConfig config) {
    DeviceConfig device = config.device();

    XCUITestOptions options =
        new XCUITestOptions()
            .setDeviceName(device.deviceName())
            .setApp(config.appPath().toString())
            .setNewCommandTimeout(config.newCommandTimeout())
            .setWdaLaunchTimeout(WDA_LAUNCH_TIMEOUT);

    if (device.targetType() == TargetType.SIMULATOR) {
      options.setConnectHardwareKeyboard(true).setForceSimulatorSoftwareKeyboardPresence(false);

      options.setCapability("appium:connectHardwareKeyboard", true);
    }

    device.udid().ifPresent(options::setUdid);
    device.platformVersion().ifPresent(options::setPlatformVersion);
    device.wdaLocalPort().ifPresent(options::setWdaLocalPort);

    return options;
  }
}
