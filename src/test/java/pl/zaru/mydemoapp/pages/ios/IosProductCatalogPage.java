package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.contracts.ProductDetailsPage;
import pl.zaru.mydemoapp.testdata.model.TestProduct;

public final class IosProductCatalogPage extends BasePage implements ProductCatalogPage {

  private static final By CATALOG_SCREEN = AppiumBy.accessibilityId("Catalog-screen");

  public IosProductCatalogPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(CATALOG_SCREEN).isDisplayed();
  }

  @Override
  public ProductDetailsPage openProduct(TestProduct product) {
    String productName = Objects.requireNonNull(product, "product must not be null").iosName();

    String escapedProductName = productName.replace("\\", "\\\\").replace("'", "\\'");

    By productNameLabel =
        AppiumBy.iOSNsPredicateString(
            "type == 'XCUIElementTypeStaticText' " + "AND label == '" + escapedProductName + "'");

    tap(productNameLabel);

    return new IosProductDetailsPage(driver());
  }
}
