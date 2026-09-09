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
import pl.zaru.mydemoapp.pages.contracts.CartPage;
import pl.zaru.mydemoapp.pages.contracts.LoginPage;
import pl.zaru.mydemoapp.testdata.model.TestProduct;
import pl.zaru.mydemoapp.tests.TestGroups;

@Epic("My Demo App")
@Feature("Authentication")
public final class CheckoutAuthenticationIT extends BaseTest {

  @Story("Require authentication before checkout")
  @Severity(SeverityLevel.CRITICAL)
  @Test(groups = {TestGroups.REGRESSION, TestGroups.AUTHENTICATION})
  public void shouldRequireLoginBeforeCheckout() {

    ScreenFactory screens = screenFactory();
    CheckoutFlow checkout = new CheckoutFlow(screens);

    TestProduct product = TestProduct.BACKPACK;

    CartPage cartPage = checkout.openCartWith(product);

    cartPage.proceedToCheckout();

    LoginPage loginPage = screens.loginPage();

    assertTrue(
        loginPage.isLoaded(),
        "Unauthenticated user should be redirected to login before checkout.");
  }
}
