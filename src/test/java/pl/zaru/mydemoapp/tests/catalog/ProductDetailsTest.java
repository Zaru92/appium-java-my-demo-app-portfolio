package pl.zaru.mydemoapp.tests.catalog;

import static org.testng.Assert.assertTrue;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.pages.ScreenFactory;
import pl.zaru.mydemoapp.pages.contracts.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.contracts.ProductDetailsPage;
import pl.zaru.mydemoapp.testdata.model.TestProduct;
import pl.zaru.mydemoapp.tests.TestGroups;

@Epic("My Demo App")
@Feature("Catalog")
public final class ProductDetailsTest extends BaseTest {

  @Story("View product details")
  @Severity(SeverityLevel.CRITICAL)
  @Test(groups = {TestGroups.SMOKE, TestGroups.REGRESSION, TestGroups.CATALOG})
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
