package pl.zaru.mydemoapp.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import java.util.Objects;
import java.util.function.Supplier;
import pl.zaru.mydemoapp.pages.android.AndroidAppNavigation;
import pl.zaru.mydemoapp.pages.android.AndroidLoginPage;
import pl.zaru.mydemoapp.pages.android.AndroidOrderConfirmationPage;
import pl.zaru.mydemoapp.pages.android.AndroidOrderReviewPage;
import pl.zaru.mydemoapp.pages.android.AndroidPaymentPage;
import pl.zaru.mydemoapp.pages.android.AndroidProductCatalogPage;
import pl.zaru.mydemoapp.pages.android.AndroidShippingAddressPage;
import pl.zaru.mydemoapp.pages.android.AndroidWebViewPage;
import pl.zaru.mydemoapp.pages.contracts.AppNavigation;
import pl.zaru.mydemoapp.pages.contracts.LoginPage;
import pl.zaru.mydemoapp.pages.contracts.OrderConfirmationPage;
import pl.zaru.mydemoapp.pages.contracts.OrderReviewPage;
import pl.zaru.mydemoapp.pages.contracts.PaymentPage;
import pl.zaru.mydemoapp.pages.contracts.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.contracts.ShippingAddressPage;
import pl.zaru.mydemoapp.pages.contracts.WebViewPage;
import pl.zaru.mydemoapp.pages.ios.IosAppNavigation;
import pl.zaru.mydemoapp.pages.ios.IosLoginPage;
import pl.zaru.mydemoapp.pages.ios.IosOrderConfirmationPage;
import pl.zaru.mydemoapp.pages.ios.IosOrderReviewPage;
import pl.zaru.mydemoapp.pages.ios.IosPaymentPage;
import pl.zaru.mydemoapp.pages.ios.IosProductCatalogPage;
import pl.zaru.mydemoapp.pages.ios.IosShippingAddressPage;
import pl.zaru.mydemoapp.pages.ios.IosWebViewPage;

public final class ScreenFactory {

  private final AppiumDriver driver;

  public ScreenFactory(AppiumDriver driver) {
    this.driver = Objects.requireNonNull(driver, "driver must not be null");
  }

  public AppNavigation appNavigation() {
    return create(() -> new AndroidAppNavigation(driver), () -> new IosAppNavigation(driver));
  }

  public WebViewPage webViewPage() {
    return create(() -> new AndroidWebViewPage(driver), () -> new IosWebViewPage(driver));
  }

  public ProductCatalogPage productCatalogPage() {
    return create(
        () -> new AndroidProductCatalogPage(driver), () -> new IosProductCatalogPage(driver));
  }

  public LoginPage loginPage() {
    return create(() -> new AndroidLoginPage(driver), () -> new IosLoginPage(driver));
  }

  public ShippingAddressPage shippingAddressPage() {
    return create(
        () -> new AndroidShippingAddressPage(driver), () -> new IosShippingAddressPage(driver));
  }

  public PaymentPage paymentPage() {
    return create(() -> new AndroidPaymentPage(driver), () -> new IosPaymentPage(driver));
  }

  public OrderReviewPage orderReviewPage() {
    return create(() -> new AndroidOrderReviewPage(driver), () -> new IosOrderReviewPage(driver));
  }

  public OrderConfirmationPage orderConfirmationPage() {
    return create(
        () -> new AndroidOrderConfirmationPage(driver), () -> new IosOrderConfirmationPage(driver));
  }

  private <T> T create(Supplier<? extends T> androidScreen, Supplier<? extends T> iosScreen) {

    if (driver instanceof AndroidDriver) {
      return androidScreen.get();
    }

    if (driver instanceof IOSDriver) {
      return iosScreen.get();
    }

    throw new IllegalArgumentException("Unsupported driver type: " + driver.getClass().getName());
  }
}
