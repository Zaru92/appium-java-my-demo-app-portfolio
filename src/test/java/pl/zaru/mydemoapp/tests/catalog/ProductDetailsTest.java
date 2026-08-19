package pl.zaru.mydemoapp.tests.catalog;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.pages.ScreenFactory;
import pl.zaru.mydemoapp.pages.contracts.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.contracts.ProductDetailsPage;
import pl.zaru.mydemoapp.testdata.model.TestProduct;

public final class ProductDetailsTest extends BaseTest {

  @Test(groups = {"smoke", "catalog"})
  public void shouldOpenSelectedProductDetails() {
    ScreenFactory screens = new ScreenFactory(driver());
    TestProduct product = TestProduct.BACKPACK;

    ProductCatalogPage catalogPage = screens.productCatalogPage();

    assertTrue(catalogPage.isLoaded(), "Product catalog should be loaded.");

    ProductDetailsPage detailsPage = catalogPage.openProduct(product);

    assertTrue(detailsPage.isLoaded(), "Product details should be loaded.");

    assertTrue(
        detailsPage.displaysProduct(product), "Product details should show the selected product.");
  }
}
