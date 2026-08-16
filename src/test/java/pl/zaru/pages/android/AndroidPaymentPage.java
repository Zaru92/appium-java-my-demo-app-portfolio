package pl.zaru.page.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.PaymentPage;
import pl.zaru.testdata.TestPaymentCard;

public final class AndroidPaymentPage extends BasePage implements PaymentPage {

  private static final String APP_ID = "com.saucelabs.mydemoapp.android:id/";

  private static final By PAYMENT_HEADING = AppiumBy.id(APP_ID + "enterPaymentMethodTV");

  private static final By FULL_NAME = AppiumBy.id(APP_ID + "nameET");

  private static final By CARD_NUMBER = AppiumBy.id(APP_ID + "cardNumberET");

  private static final By EXPIRATION_DATE = AppiumBy.id(APP_ID + "expirationDateET");

  private static final By SECURITY_CODE = AppiumBy.id(APP_ID + "securityCodeET");

  private static final By REVIEW_ORDER_BUTTON = AppiumBy.id(APP_ID + "paymentBtn");

  public AndroidPaymentPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(PAYMENT_HEADING).isDisplayed();
  }

  @Override
  public void fillPaymentDetails(TestPaymentCard paymentCard) {
    Objects.requireNonNull(paymentCard, "paymentCard must not be null");

    replaceText(FULL_NAME, paymentCard.fullName(), "payment card full name");
    replaceText(CARD_NUMBER, paymentCard.cardNumber(), "card number");
    replaceText(EXPIRATION_DATE, paymentCard.expirationDate(), "expiration date");
    replaceText(SECURITY_CODE, paymentCard.securityCode(), "security code");

    hideAndroidKeyboardIfPresent();
  }

  @Override
  public void continueToOrderReview() {
    tap(REVIEW_ORDER_BUTTON);
  }
}
