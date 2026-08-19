package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.PaymentPage;
import pl.zaru.mydemoapp.testdata.TestPaymentCard;

public final class IosPaymentPage extends BasePage implements PaymentPage {

  private static final By PAYMENT_HEADING =
      AppiumBy.iOSNsPredicateString(
          "type == 'XCUIElementTypeStaticText' " + "AND label == 'Enter a payment method'");

  private static final By FULL_NAME = textFieldWithPlaceholder("Maxim Winter");

  private static final By CARD_NUMBER = textFieldWithPlaceholder("3258 1265 7568 7896");

  private static final By EXPIRATION_DATE = textFieldWithPlaceholder("03/25");

  private static final By SECURITY_CODE = textFieldWithPlaceholder("123");

  private static final By REVIEW_ORDER_BUTTON = AppiumBy.accessibilityId("Review Order");

  public IosPaymentPage(AppiumDriver driver) {
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

    hideIosSimulatorSoftwareKeyboardIfPresent();
  }

  @Override
  public void continueToOrderReview() {
    tap(REVIEW_ORDER_BUTTON);
  }

  private static By textFieldWithPlaceholder(String placeholder) {
    return AppiumBy.iOSNsPredicateString(
        "type == 'XCUIElementTypeTextField' " + "AND value == '" + placeholder + "'");
  }
}
