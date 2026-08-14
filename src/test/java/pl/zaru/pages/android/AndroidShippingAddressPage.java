package pl.zaru.page.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.ShippingAddressPage;

public final class AndroidShippingAddressPage extends BasePage implements ShippingAddressPage {

  private static final By SHIPPING_ADDRESS_HEADING =
      AppiumBy.id("com.saucelabs.mydemoapp.android:id/enterShippingAddressTV");

  public AndroidShippingAddressPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(SHIPPING_ADDRESS_HEADING).isDisplayed();
  }
}
