package pl.zaru.page.android;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.ProductCatalogPage;

public final class AndroidProductCatalogPage extends BasePage implements ProductCatalogPage {

  private static final By PRODUCT_LIST = By.id("com.saucelabs.mydemoapp.android:id/productRV");

  public AndroidProductCatalogPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(PRODUCT_LIST).isDisplayed();
  }
}
