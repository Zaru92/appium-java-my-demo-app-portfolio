package pl.zaru.page.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.PaymentPage;

public final class IosPaymentPage extends BasePage implements PaymentPage {

  private static final By PAYMENT_SCREEN = AppiumBy.accessibilityId("Payment-screen");

  public IosPaymentPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(PAYMENT_SCREEN).isDisplayed();
  }
}
