package pl.zaru.page;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import pl.zaru.page.android.AndroidPaymentPage;
import pl.zaru.page.ios.IosPaymentPage;

public final class PaymentPageFactory {

  private PaymentPageFactory() {}

  public static PaymentPage create(AppiumDriver driver) {
    if (driver instanceof AndroidDriver) {
      return new AndroidPaymentPage(driver);
    }

    if (driver instanceof IOSDriver) {
      return new IosPaymentPage(driver);
    }

    throw new IllegalArgumentException("Unsupported driver: " + driver.getClass().getName());
  }
}
