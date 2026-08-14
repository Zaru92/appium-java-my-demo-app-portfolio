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
}
