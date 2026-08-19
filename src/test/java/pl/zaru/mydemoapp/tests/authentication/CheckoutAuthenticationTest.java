package pl.zaru.mydemoapp.tests.authentication;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.pages.ScreenFactory;
import pl.zaru.mydemoapp.pages.contracts.CartPage;
import pl.zaru.mydemoapp.pages.contracts.LoginPage;
import pl.zaru.mydemoapp.pages.contracts.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.contracts.ProductDetailsPage;
import pl.zaru.mydemoapp.testdata.model.TestProduct;

public final class CheckoutAuthenticationTest extends BaseTest {

  @Test
  public void shouldRequireLoginBeforeCheckout() {

    ScreenFactory screens = new ScreenFactory(driver());

    String productName = TestProduct.BACKPACK.nameFor(driver());

    ProductCatalogPage catalogPage = screens.productCatalogPage();

    ProductDetailsPage detailsPage = catalogPage.openProduct(productName);

    detailsPage.addToCart();

    CartPage cartPage = detailsPage.openCart();

    assertTrue(cartPage.isLoaded(), "Cart should be displayed.");

    cartPage.proceedToCheckout();

    LoginPage loginPage = screens.loginPage();

    assertTrue(
        loginPage.isLoaded(),
        "Unauthenticated user should be redirected to login before checkout.");
  }
}
