package pl.zaru.mydemoapp.driver;

import io.appium.java_client.android.options.UiAutomator2Options;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.TestConfig;

final class AndroidOptionsFactory {

  private AndroidOptionsFactory() {}

  static UiAutomator2Options create(TestConfig config) {
    DeviceConfig device = config.device();

    UiAutomator2Options options =
        new UiAutomator2Options()
            .setDeviceName(device.deviceName())
            .setApp(config.appPath().toString())
            .setNewCommandTimeout(config.newCommandTimeout());

    device.udid().ifPresent(options::setUdid);
    device.platformVersion().ifPresent(options::setPlatformVersion);
    config.appWaitActivity().ifPresent(options::setAppWaitActivity);
    device.systemPort().ifPresent(options::setSystemPort);

    return options;
  }
}
