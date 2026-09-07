package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.actions.IosActions;
import pl.zaru.mydemoapp.config.TargetType;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.PaymentPage;
import pl.zaru.mydemoapp.pages.contracts.PaymentValidation;
import pl.zaru.mydemoapp.testdata.model.TestPaymentCard;

public final class IosPaymentPage extends BasePage implements PaymentPage {

  private static final By PAYMENT_HEADING =
      AppiumBy.iOSNsPredicateString(
          "type == 'XCUIElementTypeStaticText' " + "AND label == 'Enter a payment method'");

  private static final By FULL_NAME = textFieldWithPlaceholder("Maxim Winter");

  private static final By CARD_NUMBER = textFieldWithPlaceholder("3258 1265 7568 7896");

  private static final By EXPIRATION_DATE = textFieldWithPlaceholder("03/25");

  private static final By SECURITY_CODE = textFieldWithPlaceholder("123");

  private static final By REVIEW_ORDER_BUTTON = AppiumBy.accessibilityId("Review Order");

  private final IosActions iosActions;

  private final IosValidationAlert validationAlert;

  public IosPaymentPage(AppiumDriver driver, TargetType targetType) {
    super(driver);
    iosActions = new IosActions(driver, targetType);
    validationAlert = new IosValidationAlert(driver);
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

    iosActions.hideKeyboardIfPresent();
  }

  @Override
  public void continueToOrderReview() {
    iosActions.scrollTo(REVIEW_ORDER_BUTTON);
    tap(REVIEW_ORDER_BUTTON);
  }

  @Override
  public boolean isValidationDisplayed(PaymentValidation validation) {
    String message =
        switch (Objects.requireNonNull(validation, "validation must not be null")) {
          case CARD_NUMBER_REQUIRED -> "Value looks invalid.";
        };

    return validationAlert.isDisplayed(message);
  }

  @Override
  public void dismissValidationIfPresent() {
    validationAlert.dismissIfPresent();
  }

  @Override
  public boolean isFormDisplayed() {
    return waitUntilVisible(REVIEW_ORDER_BUTTON).isDisplayed();
  }

  private static By textFieldWithPlaceholder(String placeholder) {
    return AppiumBy.iOSNsPredicateString(
        "type == 'XCUIElementTypeTextField' " + "AND value == '" + placeholder + "'");
  }
}
