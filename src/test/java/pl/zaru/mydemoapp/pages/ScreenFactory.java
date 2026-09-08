package pl.zaru.mydemoapp.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import java.util.Objects;
import java.util.function.Supplier;
import pl.zaru.mydemoapp.config.Platform;
import pl.zaru.mydemoapp.config.TestConfig;
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
import pl.zaru.mydemoapp.pages.web.ExampleDomainPage;

/**
 * Resolves platform-specific pages when a test needs to access the current screen directly.
 * Deterministic transitions continue to return the next page from the originating page object.
 */
public final class ScreenFactory {

  private final PageContext pageContext;

  private final Platform platform;

  public ScreenFactory(AppiumDriver driver, TestConfig config) {
    AppiumDriver requiredDriver = Objects.requireNonNull(driver, "driver must not be null");
    TestConfig requiredConfig = Objects.requireNonNull(config, "config must not be null");

    platform = requiredConfig.platform();
    pageContext =
        new PageContext(
            requiredDriver, requiredConfig.device().targetType(), requiredConfig.waitTimeout());

    boolean compatibleDriver =
        switch (platform) {
          case ANDROID -> requiredDriver instanceof AndroidDriver;
          case IOS -> requiredDriver instanceof IOSDriver;
        };

    if (!compatibleDriver) {
      throw new IllegalArgumentException(
          "Driver type %s does not match configured platform %s."
              .formatted(requiredDriver.getClass().getName(), platform.value()));
    }
  }

  public AppNavigation appNavigation() {
    return create(
        () -> new AndroidAppNavigation(pageContext), () -> new IosAppNavigation(pageContext));
  }

  public WebViewPage webViewPage() {
    return create(() -> new AndroidWebViewPage(pageContext), () -> new IosWebViewPage(pageContext));
  }

  public ProductCatalogPage productCatalogPage() {
    return create(
        () -> new AndroidProductCatalogPage(pageContext),
        () -> new IosProductCatalogPage(pageContext));
  }

  public LoginPage loginPage() {
    return create(() -> new AndroidLoginPage(pageContext), () -> new IosLoginPage(pageContext));
  }

  public ShippingAddressPage shippingAddressPage() {
    return create(
        () -> new AndroidShippingAddressPage(pageContext),
        () -> new IosShippingAddressPage(pageContext));
  }

  public PaymentPage paymentPage() {
    return create(() -> new AndroidPaymentPage(pageContext), () -> new IosPaymentPage(pageContext));
  }

  public OrderReviewPage orderReviewPage() {
    return create(
        () -> new AndroidOrderReviewPage(pageContext), () -> new IosOrderReviewPage(pageContext));
  }

  public OrderConfirmationPage orderConfirmationPage() {
    return create(
        () -> new AndroidOrderConfirmationPage(pageContext),
        () -> new IosOrderConfirmationPage(pageContext));
  }

  public ExampleDomainPage exampleDomainPage() {
    return new ExampleDomainPage(pageContext);
  }

  private <T> T create(Supplier<? extends T> androidScreen, Supplier<? extends T> iosScreen) {

    return switch (platform) {
      case ANDROID -> androidScreen.get();
      case IOS -> iosScreen.get();
    };
  }
}
