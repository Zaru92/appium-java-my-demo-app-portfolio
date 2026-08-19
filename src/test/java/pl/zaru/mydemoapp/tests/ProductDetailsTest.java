package pl.zaru.mydemoapp.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.pages.ScreenFactory;
import pl.zaru.mydemoapp.pages.contracts.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.contracts.ProductDetailsPage;
import pl.zaru.mydemoapp.testdata.TestProduct;

public final class ProductDetailsTest extends BaseTest {

  private static final String ANDROID_PRODUCT_NAME = "Sauce Labs Backpack";
  private static final String IOS_PRODUCT_NAME = "Sauce Labs Backpack - Black";

  @Test(groups = {"smoke", "catalog"})
  public void shouldOpenSelectedProductDetails() {

    ScreenFactory screens = new ScreenFactory(driver());

    String productName = TestProduct.BACKPACK.nameFor(driver());

    ProductCatalogPage catalogPage = screens.productCatalogPage();

    assertTrue(catalogPage.isLoaded(), "Product catalog should be loaded.");

    ProductDetailsPage detailsPage = catalogPage.openProduct(productName);

    assertTrue(detailsPage.isLoaded(), "Product details should be loaded.");

    assertEquals(
        detailsPage.displayedProductName(),
        productName,
        "Product details should show the selected product.");
  }

  private String productNameForCurrentPlatform() {
    if (driver() instanceof AndroidDriver) {
      return ANDROID_PRODUCT_NAME;
    }

    if (driver() instanceof IOSDriver) {
      return IOS_PRODUCT_NAME;
    }

    throw new IllegalStateException("Unsupported driver type: " + driver().getClass().getName());
  }
}
