package pl.zaru.mydemoapp.pages.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.actions.AndroidActions;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.PaymentPage;
import pl.zaru.mydemoapp.pages.contracts.PaymentValidation;
import pl.zaru.mydemoapp.testdata.model.TestPaymentCard;

public final class AndroidPaymentPage extends BasePage implements PaymentPage {

  private static final String APP_ID = "com.saucelabs.mydemoapp.android:id/";

  private static final By PAYMENT_HEADING = AppiumBy.id(APP_ID + "enterPaymentMethodTV");

  private static final By FULL_NAME = AppiumBy.id(APP_ID + "nameET");

  private static final By CARD_NUMBER = AppiumBy.id(APP_ID + "cardNumberET");

  private static final By EXPIRATION_DATE = AppiumBy.id(APP_ID + "expirationDateET");

  private static final By SECURITY_CODE = AppiumBy.id(APP_ID + "securityCodeET");

  private static final By REVIEW_ORDER_BUTTON = AppiumBy.id(APP_ID + "paymentBtn");

  private static final By CARD_NUMBER_ERROR = AppiumBy.id(APP_ID + "cardNumberErrorIV");

  private final AndroidActions androidActions;

  public AndroidPaymentPage(AppiumDriver driver) {
    super(driver);
    androidActions = new AndroidActions(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(PAYMENT_HEADING).isDisplayed();
  }

  @Override
  public void fillPaymentDetails(TestPaymentCard paymentCard) {
    Objects.requireNonNull(paymentCard, "paymentCard must not be null");

    replaceTextAllowingEmpty(FULL_NAME, paymentCard.fullName(), "payment card full name");
    replaceTextAllowingEmpty(CARD_NUMBER, paymentCard.cardNumber(), "card number");
    replaceTextAllowingEmpty(EXPIRATION_DATE, paymentCard.expirationDate(), "expiration date");
    replaceTextAllowingEmpty(SECURITY_CODE, paymentCard.securityCode(), "security code");

    androidActions.hideKeyboardIfPresent();
  }

  @Override
  public void continueToOrderReview() {
    tap(REVIEW_ORDER_BUTTON);
  }

  @Override
  public boolean isValidationDisplayed(PaymentValidation validation) {
    By locator =
        switch (Objects.requireNonNull(validation, "validation must not be null")) {
          case CARD_NUMBER_REQUIRED -> CARD_NUMBER_ERROR;
        };

    return waitUntilVisible(locator).isDisplayed();
  }

  @Override
  public void dismissValidationIfPresent() {
    // Android displays inline errors, so there is no modal to close.
  }

  @Override
  public boolean isFormDisplayed() {
    return waitUntilVisible(CARD_NUMBER).isDisplayed();
  }
}
