package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.ShippingAddressPage;
import pl.zaru.mydemoapp.testdata.TestAddress;

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
  ;

  public IosShippingAddressPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(SHIPPING_ADDRESS_HEADING).isDisplayed();
  }

  @Override
  public void fillAddress(TestAddress address) {
    replaceText(FULL_NAME, address.fullName(), "full name");
    replaceText(ADDRESS_LINE_1, address.addressLine1(), "address line 1");
    replaceText(ADDRESS_LINE_2, address.addressLine2(), "address line 2");
    replaceText(CITY, address.city(), "city");
    replaceText(STATE, address.state(), "state");

    scrollToIosElement(ZIP_CODE);
    replaceText(ZIP_CODE, address.zipCode(), "zip code");

    scrollToIosElement(COUNTRY);
    replaceText(COUNTRY, address.country(), "country");
  }

  @Override
  public void continueToPayment() {
    hideIosSimulatorSoftwareKeyboardIfPresent();
    tap(TO_PAYMENT_BUTTON);
  }
}
