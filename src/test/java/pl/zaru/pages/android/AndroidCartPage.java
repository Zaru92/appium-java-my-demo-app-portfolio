package pl.zaru.page.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.CartPage;

public final class AndroidCartPage extends BasePage implements CartPage {

  private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

  private static final By CART_CONTENT = AppiumBy.id(APP_PACKAGE + ":id/cartCL");

  private static final String CART_ITEM_TITLE_ID = APP_PACKAGE + ":id/titleTV";

  private static final By FIRST_PRODUCT_QUANTITY = AppiumBy.id(APP_PACKAGE + ":id/noTV");

  private static final By FIRST_PRODUCT_INCREMENT = AppiumBy.id(APP_PACKAGE + ":id/plusIV");

  private static final By FIRST_PRODUCT_REMOVE = AppiumBy.id(APP_PACKAGE + ":id/removeBt");

  private static final By EMPTY_CART = AppiumBy.id(APP_PACKAGE + ":id/noItemCL");

  private static final By PROCEED_TO_CHECKOUT_BUTTON = AppiumBy.id(APP_PACKAGE + ":id/cartBt");

  public AndroidCartPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(CART_CONTENT).isDisplayed();
  }

  @Override
  public boolean containsProduct(String productName) {
    String normalized = requireNonBlank(productName, "productName");
    String escaped = normalized.replace("\\", "\\\\").replace("\"", "\\\"");

    By product =
        AppiumBy.androidUIAutomator(
            "new UiSelector()"
                + ".resourceId(\""
                + CART_ITEM_TITLE_ID
                + "\")"
                + ".text(\""
                + escaped
                + "\")");

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
