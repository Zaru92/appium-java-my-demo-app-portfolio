package pl.zaru.mydemoapp.pages.factories;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import pl.zaru.mydemoapp.pages.android.AndroidLoginPage;
import pl.zaru.mydemoapp.pages.contracts.LoginPage;
import pl.zaru.mydemoapp.pages.ios.IosLoginPage;

public final class LoginPageFactory {

  private LoginPageFactory() {}

  public static LoginPage create(AppiumDriver driver) {
    if (driver instanceof AndroidDriver) {
      return new AndroidLoginPage(driver);
    }

    if (driver instanceof IOSDriver) {
      return new IosLoginPage(driver);
    }

    throw new IllegalArgumentException("Unsupported driver type: " + driver.getClass().getName());
  }
}
