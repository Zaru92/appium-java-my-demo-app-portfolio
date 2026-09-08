package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.actions.IosActions;
import pl.zaru.mydemoapp.pages.PageContext;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.ShippingAddressPage;
import pl.zaru.mydemoapp.pages.contracts.ShippingAddressValidation;
import pl.zaru.mydemoapp.testdata.model.TestAddress;

public final class IosShippingAddressPage extends BasePage implements ShippingAddressPage {

  private static final By SHIPPING_ADDRESS_HEADING =
      AppiumBy.iOSNsPredicateString(
          "type == 'XCUIElementTypeStaticText' " + "AND label == 'Enter a shipping address'");

  private static final By FULL_NAME = textFieldWithPlaceholder("Rebecca Winter");

  private static final By ADDRESS_LINE_1 = textFieldWithPlaceholder("Mandorley 112");

  private static final By ADDRESS_LINE_2 = textFieldWithPlaceholder("Entrance 1");

  private static final By CITY = textFieldWithPlaceholder("Truro");

  private static final By STATE = textFieldWithPlaceholder("Cornwall");

  private static final By ZIP_CODE = textFieldWithPlaceholder("89750");

  private static final By COUNTRY = textFieldWithPlaceholder("United Kingdom");

  private static final By TO_PAYMENT_BUTTON = AppiumBy.accessibilityId("To Payment");

  private static By textFieldWithPlaceholder(String placeholder) {
    return AppiumBy.iOSNsPredicateString(
        "type == 'XCUIElementTypeTextField' " + "AND value == '" + placeholder + "'");
  }

  private final IosActions iosActions;

  private final IosValidationAlert validationAlert;

  public IosShippingAddressPage(PageContext context) {
    super(context);
    iosActions = new IosActions(driver(), context.targetType());
    validationAlert = new IosValidationAlert(context);
  }

  @Override
  public boolean isLoaded() {
    return isVisible(SHIPPING_ADDRESS_HEADING);
  }

  @Override
  public void fillAddress(TestAddress address) {
    Allure.step(
        "Fill shipping address",
        () -> {
          TestAddress requiredAddress = Objects.requireNonNull(address, "address must not be null");

          replaceTextAllowingEmpty(FULL_NAME, requiredAddress.fullName(), "full name");
          replaceTextAllowingEmpty(
              ADDRESS_LINE_1, requiredAddress.addressLine1(), "address line 1");
          replaceTextAllowingEmpty(
              ADDRESS_LINE_2, requiredAddress.addressLine2(), "address line 2");
          replaceTextAllowingEmpty(CITY, requiredAddress.city(), "city");
          replaceTextAllowingEmpty(STATE, requiredAddress.state(), "state");

          iosActions.scrollTo(ZIP_CODE);
          replaceTextAllowingEmpty(ZIP_CODE, requiredAddress.zipCode(), "zip code");

          iosActions.scrollTo(COUNTRY);
          replaceTextAllowingEmpty(COUNTRY, requiredAddress.country(), "country");
        });
  }

  @Override
  @Step("Continue to payment")
  public void continueToPayment() {
    iosActions.hideKeyboardIfPresent();
    iosActions.scrollTo(TO_PAYMENT_BUTTON);
    tap(TO_PAYMENT_BUTTON);
  }

  @Override
  public boolean isValidationDisplayed(ShippingAddressValidation validation) {
    String message =
        switch (Objects.requireNonNull(validation, "validation must not be null")) {
          case ZIP_CODE_REQUIRED -> "Please provide your zip.";
        };

    return validationAlert.isDisplayed(message);
  }

  @Override
  public void dismissValidationIfPresent() {
    validationAlert.dismissIfPresent();
  }

  @Override
  public boolean isFormDisplayed() {
    return isVisible(TO_PAYMENT_BUTTON);
  }
}
