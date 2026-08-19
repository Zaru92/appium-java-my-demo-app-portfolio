package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.CartPage;

public final class IosCartPage extends BasePage implements CartPage {

  private static final By CART_SCREEN = AppiumBy.accessibilityId("Cart-screen");

  private static final By FIRST_PRODUCT_QUANTITY =
      AppiumBy.xpath(
          "(//XCUIElementTypeCell[1]"
              + "//XCUIElementTypeStaticText["
              + "string-length(@label) > 0"
              + " and string-length("
              + "translate(@label, '0123456789', '')"
              + ") = 0"
              + "])[1]");

  private static final By FIRST_PRODUCT_INCREMENT = AppiumBy.accessibilityId("AddPlus Icons");

  private static final By FIRST_PRODUCT_REMOVE = AppiumBy.accessibilityId("Remove Item");

  private static final By EMPTY_CART = AppiumBy.accessibilityId("No Items");

  private static final By PROCEED_TO_CHECKOUT_BUTTON =
      AppiumBy.accessibilityId("ProceedToCheckout");

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

  @Override
  public int firstProductQuantity() {
    String quantity = waitUntilVisible(FIRST_PRODUCT_QUANTITY).getText();
    return Integer.parseInt(quantity);
  }

  @Override
  public void increaseFirstProductQuantity() {
    int expectedQuantity = firstProductQuantity() + 1;

    tap(FIRST_PRODUCT_INCREMENT);

    waitUntilTextEquals(FIRST_PRODUCT_QUANTITY, String.valueOf(expectedQuantity));
  }

  @Override
  public void removeFirstProduct() {
    tap(FIRST_PRODUCT_REMOVE);
  }

  @Override
  public boolean isEmpty() {
    return waitUntilVisible(EMPTY_CART).isDisplayed();
  }

  @Override
  public void proceedToCheckout() {
    tap(PROCEED_TO_CHECKOUT_BUTTON);
  }
}
