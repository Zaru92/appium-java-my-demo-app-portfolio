package pl.zaru.page.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.CartPage;

public final class IosCartPage extends BasePage implements CartPage {

  private static final By CART_SCREEN = AppiumBy.accessibilityId("Cart-screen");

  public IosCartPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(CART_SCREEN).isDisplayed();
  }

  @Override
  public boolean containsProduct(String productName) {
    String normalized = requireNonBlank(productName, "productName");
    String escaped = normalized.replace("\\", "\\\\").replace("'", "\\'");

    By product =
        AppiumBy.iOSNsPredicateString(
            "type == 'XCUIElementTypeStaticText'" + " AND label == '" + escaped + "'");

    return waitUntilVisible(product).isDisplayed();
  }
}
