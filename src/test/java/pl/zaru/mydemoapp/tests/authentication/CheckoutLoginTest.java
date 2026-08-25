package pl.zaru.mydemoapp.tests.authentication;

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
import pl.zaru.mydemoapp.pages.contracts.LoginPage;
import pl.zaru.mydemoapp.pages.contracts.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.contracts.ProductDetailsPage;
import pl.zaru.mydemoapp.pages.contracts.ShippingAddressPage;
import pl.zaru.mydemoapp.testdata.factory.UserFactory;
import pl.zaru.mydemoapp.testdata.model.TestProduct;
import pl.zaru.mydemoapp.testdata.model.TestUser;
import pl.zaru.mydemoapp.tests.TestGroups;

@Epic("My Demo App")
@Feature("Authentication")
public final class CheckoutLoginTest extends BaseTest {

  @Story("Continue checkout after login")
  @Severity(SeverityLevel.BLOCKER)
  @Test(groups = {TestGroups.REGRESSION, TestGroups.AUTHENTICATION})
  public void shouldContinueCheckoutAfterValidLogin() {

    ScreenFactory screens = new ScreenFactory(driver());

    TestProduct product = TestProduct.BACKPACK;
    TestUser user = UserFactory.standardUser();

    ProductCatalogPage catalogPage = screens.productCatalogPage();

    ProductDetailsPage detailsPage = catalogPage.openProduct(product);

    detailsPage.addToCart();

    CartPage cartPage = detailsPage.openCart();
    assertTrue(cartPage.isLoaded(), "Cart should be displayed.");

    cartPage.proceedToCheckout();

    LoginPage loginPage = screens.loginPage();
    assertTrue(loginPage.isLoaded(), "Login page should be displayed.");

    loginPage.login(user.username(), user.password());

    ShippingAddressPage shippingAddressPage = screens.shippingAddressPage();

    assertTrue(
        shippingAddressPage.isLoaded(), "Shipping address page should be displayed after login.");
  }
}
