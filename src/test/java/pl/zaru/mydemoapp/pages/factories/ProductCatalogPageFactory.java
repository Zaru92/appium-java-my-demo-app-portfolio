package pl.zaru.mydemoapp.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import java.util.Objects;
import pl.zaru.mydemoapp.pages.android.AndroidProductCatalogPage;
import pl.zaru.mydemoapp.pages.ios.IosProductCatalogPage;

public final class ProductCatalogPageFactory {

  private ProductCatalogPageFactory() {}

  public static ProductCatalogPage create(AppiumDriver driver) {
    Objects.requireNonNull(driver, "driver must not be null");

    if (driver instanceof AndroidDriver) {
      return new AndroidProductCatalogPage(driver);
    }

    if (driver instanceof IOSDriver) {
      return new IosProductCatalogPage(driver);
    }

    throw new IllegalArgumentException("Unsupported Appium driver: " + driver.getClass().getName());
  }
}
