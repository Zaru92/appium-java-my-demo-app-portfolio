package pl.zaru.mydemoapp.tests;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.pages.contracts.CartPage;
import pl.zaru.mydemoapp.pages.contracts.LoginPage;
import pl.zaru.mydemoapp.pages.contracts.OrderConfirmationPage;
import pl.zaru.mydemoapp.pages.contracts.OrderReviewPage;
import pl.zaru.mydemoapp.pages.contracts.PaymentPage;
import pl.zaru.mydemoapp.pages.contracts.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.contracts.ProductDetailsPage;
import pl.zaru.mydemoapp.pages.contracts.ShippingAddressPage;
import pl.zaru.mydemoapp.pages.factories.LoginPageFactory;
import pl.zaru.mydemoapp.pages.factories.OrderConfirmationPageFactory;
import pl.zaru.mydemoapp.pages.factories.OrderReviewPageFactory;
import pl.zaru.mydemoapp.pages.factories.PaymentPageFactory;
import pl.zaru.mydemoapp.pages.factories.ProductCatalogPageFactory;
import pl.zaru.mydemoapp.pages.factories.ShippingAddressPageFactory;
import pl.zaru.mydemoapp.testdata.TestAddress;
import pl.zaru.mydemoapp.testdata.TestPaymentCard;
import pl.zaru.mydemoapp.testdata.TestProduct;
import pl.zaru.mydemoapp.testdata.TestUser;

public final class CheckoutOrderConfirmationTest extends BaseTest {

  @Test
  public void shouldConfirmOrderAfterPlacingIt() {
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

    shippingAddressPage.fillAddress(TestAddress.DEFAULT);
    shippingAddressPage.continueToPayment();

    PaymentPage paymentPage = PaymentPageFactory.create(driver());
    assertTrue(paymentPage.isLoaded(), "Payment page should be displayed.");

    paymentPage.fillPaymentDetails(TestPaymentCard.DEFAULT);
    paymentPage.continueToOrderReview();

    OrderReviewPage orderReviewPage = OrderReviewPageFactory.create(driver());

    assertTrue(orderReviewPage.isLoaded(), "Order review page should be displayed.");

    orderReviewPage.placeOrder();

    OrderConfirmationPage confirmationPage = OrderConfirmationPageFactory.create(driver());

    assertTrue(confirmationPage.isLoaded(), "Order confirmation page should be displayed.");

    String confirmationMessage = confirmationPage.confirmationMessage();

    assertTrue(
        confirmationMessage.startsWith("Thank you for your order"),
        "Unexpected confirmation message: " + confirmationMessage);
  }
}
