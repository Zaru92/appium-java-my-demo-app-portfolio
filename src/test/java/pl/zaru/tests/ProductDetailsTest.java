package pl.zaru.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.testng.annotations.Test;
import pl.zaru.base.BaseTest;
import pl.zaru.page.ProductCatalogPage;
import pl.zaru.page.ProductCatalogPageFactory;
import pl.zaru.page.ProductDetailsPage;
import pl.zaru.testdata.TestProduct;

public final class ProductDetailsTest extends BaseTest {

  private static final String ANDROID_PRODUCT_NAME = "Sauce Labs Backpack";
  private static final String IOS_PRODUCT_NAME = "Sauce Labs Backpack - Black";

  @Test(groups = {"smoke", "catalog"})
  public void shouldOpenSelectedProductDetails() {

    String productName = TestProduct.BACKPACK.nameFor(driver());

    ProductCatalogPage catalogPage = ProductCatalogPageFactory.create(driver());

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
