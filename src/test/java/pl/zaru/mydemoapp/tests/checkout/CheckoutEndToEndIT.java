package pl.zaru.mydemoapp.tests.checkout;

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
import pl.zaru.mydemoapp.pages.contracts.OrderConfirmationPage;
import pl.zaru.mydemoapp.pages.contracts.OrderReviewPage;
import pl.zaru.mydemoapp.testdata.factory.AddressFactory;
import pl.zaru.mydemoapp.testdata.factory.PaymentCardFactory;
import pl.zaru.mydemoapp.testdata.factory.UserFactory;
import pl.zaru.mydemoapp.testdata.model.TestProduct;
import pl.zaru.mydemoapp.testdata.model.TestUser;
import pl.zaru.mydemoapp.tests.TestGroups;

@Epic("My Demo App")
@Feature("Checkout")
public final class CheckoutEndToEndIT extends BaseTest {

  @Story("Complete checkout successfully")
  @Severity(SeverityLevel.BLOCKER)
  @Test(groups = {TestGroups.SMOKE, TestGroups.REGRESSION, TestGroups.E2E, TestGroups.CHECKOUT})
  public void shouldCompleteCheckoutSuccessfully() {
    ScreenFactory screens = screenFactory();
    CheckoutFlow checkout = new CheckoutFlow(screens);

    TestProduct product = TestProduct.BACKPACK;
    TestUser user = UserFactory.standardUser();

    OrderReviewPage orderReviewPage =
        checkout.openOrderReviewFor(
            product,
            user,
            AddressFactory.validShippingAddress(),
            PaymentCardFactory.validVisaCard());

    orderReviewPage.placeOrder();

    OrderConfirmationPage confirmationPage = screens.orderConfirmationPage();

    assertTrue(confirmationPage.isLoaded(), "Order confirmation page should be displayed.");

    String confirmationMessage = confirmationPage.confirmationMessage();

    assertTrue(
        confirmationMessage.startsWith("Thank you for your order"),
        "Unexpected confirmation message: " + confirmationMessage);
  }
}
