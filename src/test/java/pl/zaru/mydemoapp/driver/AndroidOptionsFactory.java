package pl.zaru.mydemoapp.driver;

import io.appium.java_client.android.options.UiAutomator2Options;
import pl.zaru.mydemoapp.config.TestConfig;

final class AndroidOptionsFactory {

  private AndroidOptionsFactory() {}

  static UiAutomator2Options create(TestConfig config) {
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
}
