package pl.zaru.mydemoapp.pages.android;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.actions.AndroidActions;
import pl.zaru.mydemoapp.pages.PageContext;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.ShippingAddressPage;
import pl.zaru.mydemoapp.pages.contracts.ShippingAddressValidation;
import pl.zaru.mydemoapp.testdata.model.TestAddress;

public final class AndroidShippingAddressPage extends BasePage implements ShippingAddressPage {

  private static final By SHIPPING_ADDRESS_HEADING =
      AppiumBy.id("com.saucelabs.mydemoapp.android:id/enterShippingAddressTV");

  private static final String APP_ID = "com.saucelabs.mydemoapp.android";

  private static final By PAYMENT_BUTTON = AppiumBy.id(APP_ID + ":id/paymentBtn");

  private static By scrollTo(String elementId) {
    String resourceId = APP_ID + ":id/" + elementId;

    return AppiumBy.androidUIAutomator(
        "new UiScrollable(new UiSelector().scrollable(true))"
            + ".scrollIntoView(new UiSelector().resourceId(\""
            + resourceId
            + "\"))");
  }

  private final AndroidActions androidActions;

  public AndroidShippingAddressPage(PageContext context) {
    super(context);
    androidActions = new AndroidActions(driver());
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

          replaceTextAllowingEmpty(scrollTo("fullNameET"), requiredAddress.fullName(), "fullName");
          replaceTextAllowingEmpty(
              scrollTo("address1ET"), requiredAddress.addressLine1(), "addressLine1");
          replaceTextAllowingEmpty(
              scrollTo("address2ET"), requiredAddress.addressLine2(), "addressLine2");
          replaceTextAllowingEmpty(scrollTo("cityET"), requiredAddress.city(), "city");
          replaceTextAllowingEmpty(scrollTo("stateET"), requiredAddress.state(), "state");
          replaceTextAllowingEmpty(scrollTo("zipET"), requiredAddress.zipCode(), "zipCode");
          replaceTextAllowingEmpty(scrollTo("countryET"), requiredAddress.country(), "country");
        });
  }

  @Override
  @Step("Continue to payment")
  public void continueToPayment() {
    androidActions.hideKeyboardIfPresent();
    tap(PAYMENT_BUTTON);
  }

  @Override
  public boolean isValidationDisplayed(ShippingAddressValidation validation) {
    By locator =
        switch (Objects.requireNonNull(validation, "validation must not be null")) {
          case ZIP_CODE_REQUIRED -> scrollTo("zipErrorTV");
        };

    return isVisible(locator);
  }

  @Override
  public void dismissValidationIfPresent() {
    // Android displays inline errors, so there is no modal to close.
  }

  @Override
  public boolean isFormDisplayed() {
    return isVisible(scrollTo("zipET"));
  }
}
