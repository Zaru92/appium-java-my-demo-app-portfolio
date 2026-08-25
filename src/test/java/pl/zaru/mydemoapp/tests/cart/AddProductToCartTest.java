package pl.zaru.mydemoapp.tests.cart;

import static org.testng.Assert.assertTrue;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.pages.ScreenFactory;
import pl.zaru.mydemoapp.pages.contracts.CartPage;
import pl.zaru.mydemoapp.pages.contracts.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.contracts.ProductDetailsPage;
import pl.zaru.mydemoapp.testdata.model.TestProduct;
import pl.zaru.mydemoapp.tests.TestGroups;

@Epic("My Demo App")
@Feature("Cart")
public final class AddProductToCartTest extends BaseTest {

  @Story("Add product to cart")
  @Severity(SeverityLevel.CRITICAL)
  @Test(groups = {TestGroups.SMOKE, TestGroups.REGRESSION, TestGroups.CART})
  public void shouldAddSelectedProductToCart() {

    ScreenFactory screens = new ScreenFactory(driver());

    TestProduct product = TestProduct.BACKPACK;

    ProductCatalogPage catalogPage = screens.productCatalogPage();

    assertTrue(catalogPage.isLoaded(), "Product catalog should be loaded.");

    ProductDetailsPage detailsPage = catalogPage.openProduct(product);

    assertTrue(detailsPage.isLoaded(), "Product details should be loaded.");

    detailsPage.addToCart();

    CartPage cartPage = detailsPage.openCart();

    assertTrue(cartPage.isLoaded(), "Cart should be loaded.");

    assertTrue(
        cartPage.containsProduct(product), "Cart should contain selected product: " + product);
  }
}
