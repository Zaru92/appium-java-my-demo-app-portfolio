package pl.zaru.mydemoapp.tests;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.pages.CartPage;
import pl.zaru.mydemoapp.pages.LoginPage;
import pl.zaru.mydemoapp.pages.LoginPageFactory;
import pl.zaru.mydemoapp.pages.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.ProductCatalogPageFactory;
import pl.zaru.mydemoapp.pages.ProductDetailsPage;
import pl.zaru.mydemoapp.testdata.TestProduct;

public final class CheckoutAuthenticationTest extends BaseTest {

  @Test
  public void shouldRequireLoginBeforeCheckout() {
    String productName = TestProduct.BACKPACK.nameFor(driver());

    ProductCatalogPage catalogPage = ProductCatalogPageFactory.create(driver());

    ProductDetailsPage detailsPage = catalogPage.openProduct(productName);

    detailsPage.addToCart();

    CartPage cartPage = detailsPage.openCart();

    assertTrue(cartPage.isLoaded(), "Cart should be displayed.");

    cartPage.proceedToCheckout();

    LoginPage loginPage = LoginPageFactory.create(driver());

    assertTrue(
        loginPage.isLoaded(),
        "Unauthenticated user should be redirected to login before checkout.");
  }
}
