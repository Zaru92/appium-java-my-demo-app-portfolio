package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.actions.IosActions;
import pl.zaru.mydemoapp.pages.PageContext;
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

  public IosPaymentPage(PageContext context) {
    super(context);
    iosActions = new IosActions(driver(), context.targetType());
    validationAlert = new IosValidationAlert(context);
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

          iosActions.hideKeyboardIfPresent();
        });
  }

  @Override
  @Step("Continue to order review")
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
    return isVisible(REVIEW_ORDER_BUTTON);
  }

  private static By textFieldWithPlaceholder(String placeholder) {
    return AppiumBy.iOSNsPredicateString(
        "type == 'XCUIElementTypeTextField' " + "AND value == '" + placeholder + "'");
  }
}
