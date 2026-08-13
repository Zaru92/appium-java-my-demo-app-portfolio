package pl.zaru.page.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.ProductCatalogPage;

public final class IosProductCatalogPage extends BasePage implements ProductCatalogPage {

  private static final By CATALOG_SCREEN = AppiumBy.accessibilityId("Catalog-screen");

  public IosProductCatalogPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(CATALOG_SCREEN).isDisplayed();
  }
}
