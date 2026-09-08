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
import pl.zaru.mydemoapp.pages.contracts.PaymentPage;
import pl.zaru.mydemoapp.pages.contracts.PaymentValidation;
import pl.zaru.mydemoapp.testdata.factory.AddressFactory;
import pl.zaru.mydemoapp.testdata.factory.PaymentCardFactory;
import pl.zaru.mydemoapp.testdata.factory.UserFactory;
import pl.zaru.mydemoapp.testdata.model.TestProduct;
import pl.zaru.mydemoapp.tests.TestGroups;

@Epic("My Demo App")
@Feature("Checkout")
public final class CheckoutPaymentValidationIT extends BaseTest {

  @Story("Validate required payment fields")
  @Severity(SeverityLevel.CRITICAL)
  @Test(groups = {TestGroups.REGRESSION, TestGroups.CHECKOUT})
  public void shouldRejectPaymentWithoutCardNumber() {
    CheckoutFlow checkout = new CheckoutFlow(screenFactory());

    PaymentPage paymentPage =
        checkout.openPaymentFor(
            TestProduct.BACKPACK,
            UserFactory.standardUser(),
            AddressFactory.validShippingAddress());

    paymentPage.fillPaymentDetails(PaymentCardFactory.paymentCardWithoutNumber());
    paymentPage.continueToOrderReview();

    assertTrue(
        paymentPage.isValidationDisplayed(PaymentValidation.CARD_NUMBER_REQUIRED),
        "A missing card number should be marked as invalid.");

    paymentPage.dismissValidationIfPresent();

    assertTrue(
        paymentPage.isFormDisplayed(),
        "Checkout should remain on the payment form when the card number is missing.");
  }
}
