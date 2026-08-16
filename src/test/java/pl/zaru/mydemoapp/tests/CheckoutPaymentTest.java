package pl.zaru.mydemoapp.tests;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.pages.CartPage;
import pl.zaru.mydemoapp.pages.LoginPage;
import pl.zaru.mydemoapp.pages.LoginPageFactory;
import pl.zaru.mydemoapp.pages.OrderReviewPage;
import pl.zaru.mydemoapp.pages.OrderReviewPageFactory;
import pl.zaru.mydemoapp.pages.PaymentPage;
import pl.zaru.mydemoapp.pages.PaymentPageFactory;
import pl.zaru.mydemoapp.pages.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.ProductCatalogPageFactory;
import pl.zaru.mydemoapp.pages.ProductDetailsPage;
import pl.zaru.mydemoapp.pages.ShippingAddressPage;
import pl.zaru.mydemoapp.pages.ShippingAddressPageFactory;
import pl.zaru.mydemoapp.testdata.TestAddress;
import pl.zaru.mydemoapp.testdata.TestPaymentCard;
import pl.zaru.mydemoapp.testdata.TestProduct;
import pl.zaru.mydemoapp.testdata.TestUser;

public final class CheckoutPaymentTest extends BaseTest {

  @Test
  public void shouldContinueToOrderReviewAfterProvidingPaymentDetails() {
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
  }
}
