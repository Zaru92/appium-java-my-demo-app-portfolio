package pl.zaru.mydemoapp.pages.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.ShippingAddressPage;
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

  public AndroidShippingAddressPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(SHIPPING_ADDRESS_HEADING).isDisplayed();
  }

  @Override
  public void fillAddress(TestAddress address) {
    replaceText(scrollTo("fullNameET"), address.fullName(), "fullName");
    replaceText(scrollTo("address1ET"), address.addressLine1(), "addressLine1");
    replaceText(scrollTo("address2ET"), address.addressLine2(), "addressLine2");
    replaceText(scrollTo("cityET"), address.city(), "city");
    replaceText(scrollTo("stateET"), address.state(), "state");
    replaceText(scrollTo("zipET"), address.zipCode(), "zipCode");
    replaceText(scrollTo("countryET"), address.country(), "country");
  }

  @Override
  public void continueToPayment() {
    hideAndroidKeyboardIfPresent();
    tap(PAYMENT_BUTTON);
  }
}
