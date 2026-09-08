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
import pl.zaru.mydemoapp.pages.contracts.ShippingAddressPage;
import pl.zaru.mydemoapp.pages.contracts.ShippingAddressValidation;
import pl.zaru.mydemoapp.testdata.factory.AddressFactory;
import pl.zaru.mydemoapp.testdata.factory.UserFactory;
import pl.zaru.mydemoapp.testdata.model.TestProduct;
import pl.zaru.mydemoapp.tests.TestGroups;

@Epic("My Demo App")
@Feature("Checkout")
public final class CheckoutShippingAddressValidationIT extends BaseTest {

  @Story("Validate required shipping address fields")
  @Severity(SeverityLevel.CRITICAL)
  @Test(groups = {TestGroups.REGRESSION, TestGroups.CHECKOUT})
  public void shouldRejectShippingAddressWithoutZipCode() {
    CheckoutFlow checkout = new CheckoutFlow(screenFactory());

    ShippingAddressPage shippingAddressPage =
        checkout.openShippingAddressFor(TestProduct.BACKPACK, UserFactory.standardUser());

    shippingAddressPage.fillAddress(AddressFactory.shippingAddressWithoutZipCode());
    shippingAddressPage.continueToPayment();

    assertTrue(
        shippingAddressPage.isValidationDisplayed(ShippingAddressValidation.ZIP_CODE_REQUIRED),
        "A missing ZIP code should produce a shipping address validation message.");

    shippingAddressPage.dismissValidationIfPresent();

    assertTrue(
        shippingAddressPage.isFormDisplayed(),
        "Checkout should remain on the shipping address form when the ZIP code is missing.");
  }
}
