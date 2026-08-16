package pl.zaru.mydemoapp.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import org.openqa.selenium.Capabilities;
import pl.zaru.mydemoapp.config.TestConfig;

public final class DriverFactory {
  private DriverFactory() {}

  public static AppiumDriver create(TestConfig config) {
    Objects.requireNonNull(config, "config must not be null");

    URL serverUrl = toUrl(config);
    Capabilities options = DriverOptionsFactory.create(config);

    return switch (config.platform()) {
      case ANDROID -> new AndroidDriver(serverUrl, options);
      case IOS -> new IOSDriver(serverUrl, options);
    };
  }

  private static URL toUrl(TestConfig config) {
    try {
      return config.appiumUrl().toURL();
    } catch (MalformedURLException exception) {
      throw new IllegalArgumentException(
          "Invalid Appium server URL: " + config.appiumUrl(), exception);
    }
  }
}
