package pl.zaru.tests;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;
import pl.zaru.base.BaseTest;
import pl.zaru.page.CartPage;
import pl.zaru.page.LoginPage;
import pl.zaru.page.LoginPageFactory;
import pl.zaru.page.ProductCatalogPage;
import pl.zaru.page.ProductCatalogPageFactory;
import pl.zaru.page.ProductDetailsPage;
import pl.zaru.page.ShippingAddressPage;
import pl.zaru.page.ShippingAddressPageFactory;
import pl.zaru.testdata.TestProduct;
import pl.zaru.testdata.TestUser;

public final class CheckoutLoginTest extends BaseTest {

  @Test
  public void shouldContinueCheckoutAfterValidLogin() {
    String productName = TestProduct.BACKPACK.nameFor(driver());
    TestUser user = TestUser.STANDARD;

    ProductCatalogPage catalogPage = ProductCatalogPageFactory.create(driver());

    ProductDetailsPage detailsPage = catalogPage.openProduct(productName);

    detailsPage.addToCart();

    CartPage cartPage = detailsPage.openCart();
    assertTrue(cartPage.isLoaded(), "Cart should be displayed.");

    cartPage.proceedToCheckout();

    LoginPage loginPage = LoginPageFactory.create(driver());
    assertTrue(loginPage.isLoaded(), "Login page should be displayed.");

    loginPage.login(user.username(), user.password());

    ShippingAddressPage shippingAddressPage = ShippingAddressPageFactory.create(driver());

    assertTrue(
        shippingAddressPage.isLoaded(), "Shipping address page should be displayed after login.");
  }
}
