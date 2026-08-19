package pl.zaru.mydemoapp.pages.factories;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import java.util.Objects;
import pl.zaru.mydemoapp.pages.android.AndroidOrderReviewPage;
import pl.zaru.mydemoapp.pages.contracts.OrderReviewPage;
import pl.zaru.mydemoapp.pages.ios.IosOrderReviewPage;

public final class OrderReviewPageFactory {

  private OrderReviewPageFactory() {}

  public static OrderReviewPage create(AppiumDriver driver) {
    Objects.requireNonNull(driver, "driver must not be null");

    if (driver instanceof AndroidDriver) {
      return new AndroidOrderReviewPage(driver);
    }

    if (driver instanceof IOSDriver) {
      return new IosOrderReviewPage(driver);
    }

    throw new IllegalArgumentException("Unsupported driver type: " + driver.getClass().getName());
  }
}
