package pl.zaru.page;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import pl.zaru.page.android.AndroidShippingAddressPage;
import pl.zaru.page.ios.IosShippingAddressPage;

public final class ShippingAddressPageFactory {

  private ShippingAddressPageFactory() {}

  public static ShippingAddressPage create(AppiumDriver driver) {
    if (driver instanceof AndroidDriver) {
      return new AndroidShippingAddressPage(driver);
    }

    if (driver instanceof IOSDriver) {
      return new IosShippingAddressPage(driver);
    }

    throw new IllegalArgumentException("Unsupported driver type: " + driver.getClass().getName());
  }
}
