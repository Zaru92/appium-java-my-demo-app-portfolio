package pl.zaru.mydemoapp.flows;

import static org.testng.Assert.assertTrue;

import java.util.Objects;
import pl.zaru.mydemoapp.pages.ScreenFactory;
import pl.zaru.mydemoapp.pages.contracts.CartPage;
import pl.zaru.mydemoapp.pages.contracts.LoginPage;
import pl.zaru.mydemoapp.pages.contracts.OrderReviewPage;
import pl.zaru.mydemoapp.pages.contracts.PaymentPage;
import pl.zaru.mydemoapp.pages.contracts.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.contracts.ProductDetailsPage;
import pl.zaru.mydemoapp.pages.contracts.ShippingAddressPage;
import pl.zaru.mydemoapp.testdata.model.TestAddress;
import pl.zaru.mydemoapp.testdata.model.TestPaymentCard;
import pl.zaru.mydemoapp.testdata.model.TestProduct;
import pl.zaru.mydemoapp.testdata.model.TestUser;

public final class CheckoutFlow {
  private final ScreenFactory screens;

  public CheckoutFlow(ScreenFactory screens) {
    this.screens = Objects.requireNonNull(screens, "screens must not be null");
  }

  public CartPage openCartWith(TestProduct product) {
    ProductCatalogPage catalogPage = screens.productCatalogPage();
    ProductDetailsPage detailsPage =
        catalogPage.openProduct(Objects.requireNonNull(product, "product must not be null"));

    detailsPage.addToCart();

    CartPage cartPage = detailsPage.openCart();
    assertTrue(cartPage.isLoaded(), "Cart should be displayed.");

    return cartPage;
  }

  public LoginPage openLoginFor(TestProduct product) {
    CartPage cartPage = openCartWith(product);
    cartPage.proceedToCheckout();

    LoginPage loginPage = screens.loginPage();
    assertTrue(loginPage.isLoaded(), "Login page should be displayed.");

    return loginPage;
  }

  public ShippingAddressPage openShippingAddressFor(TestProduct product, TestUser user) {
    LoginPage loginPage = openLoginFor(product);
    TestUser requiredUser = Objects.requireNonNull(user, "user must not be null");

    loginPage.login(requiredUser.username(), requiredUser.password());

    ShippingAddressPage shippingAddressPage = screens.shippingAddressPage();
    assertTrue(
        shippingAddressPage.isLoaded(), "Shipping address page should be displayed after login.");

    return shippingAddressPage;
  }

  public PaymentPage openPaymentFor(
      TestProduct product, TestUser user, TestAddress shippingAddress) {
    ShippingAddressPage shippingAddressPage = openShippingAddressFor(product, user);

    shippingAddressPage.fillAddress(
        Objects.requireNonNull(shippingAddress, "shippingAddress must not be null"));
    shippingAddressPage.continueToPayment();

    PaymentPage paymentPage = screens.paymentPage();
    assertTrue(paymentPage.isLoaded(), "Payment page should be displayed.");

    return paymentPage;
  }

  public OrderReviewPage openOrderReviewFor(
      TestProduct product,
      TestUser user,
      TestAddress shippingAddress,
      TestPaymentCard paymentCard) {
    PaymentPage paymentPage = openPaymentFor(product, user, shippingAddress);

    paymentPage.fillPaymentDetails(
        Objects.requireNonNull(paymentCard, "paymentCard must not be null"));
    paymentPage.continueToOrderReview();

    OrderReviewPage orderReviewPage = screens.orderReviewPage();
    assertTrue(orderReviewPage.isLoaded(), "Order review page should be displayed.");

    return orderReviewPage;
  }
}
