package pl.zaru.mydemoapp.tests.authentication;

import static org.testng.Assert.assertTrue;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.flows.CheckoutFlow;
import pl.zaru.mydemoapp.pages.ScreenFactory;
import pl.zaru.mydemoapp.pages.contracts.LoginPage;
import pl.zaru.mydemoapp.pages.contracts.ShippingAddressPage;
import pl.zaru.mydemoapp.testdata.factory.UserFactory;
import pl.zaru.mydemoapp.testdata.model.TestProduct;
import pl.zaru.mydemoapp.testdata.model.TestUser;
import pl.zaru.mydemoapp.tests.TestGroups;

@Epic("My Demo App")
@Feature("Authentication")
public final class CheckoutLoginIT extends BaseTest {

  @Story("Continue checkout after login")
  @Severity(SeverityLevel.BLOCKER)
  @Test(groups = {TestGroups.REGRESSION, TestGroups.AUTHENTICATION})
  public void shouldContinueCheckoutAfterValidLogin() {

    ScreenFactory screens = screenFactory();
    CheckoutFlow checkout = new CheckoutFlow(screens);

    TestProduct product = TestProduct.BACKPACK;
    TestUser user = UserFactory.standardUser();

    LoginPage loginPage = checkout.openLoginFor(product);

    loginPage.login(user.username(), user.password());

    ShippingAddressPage shippingAddressPage = screens.shippingAddressPage();

    assertTrue(
        shippingAddressPage.isLoaded(), "Shipping address page should be displayed after login.");
  }
}
