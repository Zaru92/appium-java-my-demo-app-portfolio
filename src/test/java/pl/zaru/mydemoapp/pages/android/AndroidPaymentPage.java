package pl.zaru.mydemoapp.pages.android;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.actions.AndroidActions;
import pl.zaru.mydemoapp.pages.PageContext;
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

  public AndroidPaymentPage(PageContext context) {
    super(context);
    androidActions = new AndroidActions(driver());
  }

  @Override
  public boolean isLoaded() {
    return isVisible(PAYMENT_HEADING);
  }

  @Override
  public void fillPaymentDetails(TestPaymentCard paymentCard) {
    Allure.step(
        "Fill payment details",
        () -> {
          TestPaymentCard requiredCard =
              Objects.requireNonNull(paymentCard, "paymentCard must not be null");

          replaceTextAllowingEmpty(FULL_NAME, requiredCard.fullName(), "payment card full name");
          replaceTextAllowingEmpty(CARD_NUMBER, requiredCard.cardNumber(), "card number");
          replaceTextAllowingEmpty(
              EXPIRATION_DATE, requiredCard.expirationDate(), "expiration date");
          replaceTextAllowingEmpty(SECURITY_CODE, requiredCard.securityCode(), "security code");

          androidActions.hideKeyboardIfPresent();
        });
  }

  @Override
  @Step("Continue to order review")
  public void continueToOrderReview() {
    tap(REVIEW_ORDER_BUTTON);
  }

  @Override
  public boolean isValidationDisplayed(PaymentValidation validation) {
    By locator =
        switch (Objects.requireNonNull(validation, "validation must not be null")) {
          case CARD_NUMBER_REQUIRED -> CARD_NUMBER_ERROR;
        };

    return isVisible(locator);
  }

  @Override
  public void dismissValidationIfPresent() {
    // Android displays inline errors, so there is no modal to close.
  }

  @Override
  public boolean isFormDisplayed() {
    return isVisible(CARD_NUMBER);
  }
}
