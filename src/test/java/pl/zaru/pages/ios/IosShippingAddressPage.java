package pl.zaru.page.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.ShippingAddressPage;

public final class IosShippingAddressPage extends BasePage implements ShippingAddressPage {

  private static final By SHIPPING_ADDRESS_HEADING =
      AppiumBy.iOSNsPredicateString(
          "type == 'XCUIElementTypeStaticText' " + "AND label == 'Enter a shipping address'");

  public IosShippingAddressPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(SHIPPING_ADDRESS_HEADING).isDisplayed();
  }
}
