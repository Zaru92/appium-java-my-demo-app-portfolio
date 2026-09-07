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
import pl.zaru.mydemoapp.pages.contracts.PaymentPage;
import pl.zaru.mydemoapp.pages.contracts.ShippingAddressPage;
import pl.zaru.mydemoapp.testdata.factory.AddressFactory;
import pl.zaru.mydemoapp.testdata.factory.UserFactory;
import pl.zaru.mydemoapp.testdata.model.TestProduct;
import pl.zaru.mydemoapp.testdata.model.TestUser;
import pl.zaru.mydemoapp.tests.TestGroups;

@Epic("My Demo App")
@Feature("Checkout")
public final class CheckoutShippingAddressTest extends BaseTest {

  @Story("Provide shipping address")
  @Severity(SeverityLevel.CRITICAL)
  @Test(groups = {TestGroups.REGRESSION, TestGroups.CHECKOUT})
  public void shouldContinueToPaymentAfterProvidingShippingAddress() {

    ScreenFactory screens = screenFactory();
    CheckoutFlow checkout = new CheckoutFlow(screens);

    TestProduct product = TestProduct.BACKPACK;
    TestUser user = UserFactory.standardUser();

    ShippingAddressPage shippingAddressPage = checkout.openShippingAddressFor(product, user);

    shippingAddressPage.fillAddress(AddressFactory.validShippingAddress());
    shippingAddressPage.continueToPayment();

    PaymentPage paymentPage = screens.paymentPage();

    assertTrue(paymentPage.isLoaded(), "Payment page should be displayed.");
  }
}
