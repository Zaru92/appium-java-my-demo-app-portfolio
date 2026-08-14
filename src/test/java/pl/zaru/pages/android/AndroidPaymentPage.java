package pl.zaru.page.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.PaymentPage;

public final class AndroidPaymentPage extends BasePage implements PaymentPage {

  private static final By PAYMENT_METHOD_HEADING =
      AppiumBy.id("com.saucelabs.mydemoapp.android" + ":id/enterPaymentMethodTV");

  public AndroidPaymentPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(PAYMENT_METHOD_HEADING).isDisplayed();
  }
}
