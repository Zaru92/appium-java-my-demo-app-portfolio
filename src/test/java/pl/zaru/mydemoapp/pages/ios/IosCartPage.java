package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.PageContext;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.CartPage;
import pl.zaru.mydemoapp.pages.locators.IosLocators;
import pl.zaru.mydemoapp.testdata.model.TestProduct;

public final class IosCartPage extends BasePage implements CartPage {

  private static final By CART_SCREEN = AppiumBy.accessibilityId("Cart-screen");

  private static final By FIRST_PRODUCT_QUANTITY =
      AppiumBy.iOSClassChain(
          "**/XCUIElementTypeCell[1]/**/XCUIElementTypeStaticText"
              + "[`label MATCHES '^[0-9]+$'`][1]");

  private static final By FIRST_PRODUCT_INCREMENT = AppiumBy.accessibilityId("AddPlus Icons");

  private static final By FIRST_PRODUCT_REMOVE = AppiumBy.accessibilityId("Remove Item");

  private static final By EMPTY_CART = AppiumBy.accessibilityId("No Items");

  private static final By PROCEED_TO_CHECKOUT_BUTTON =
      AppiumBy.accessibilityId("ProceedToCheckout");

  public IosCartPage(PageContext context) {
    super(context);
  }

  @Override
  public boolean isLoaded() {
    return isVisible(CART_SCREEN);
  }

  @Override
  public boolean containsProduct(TestProduct product) {
    String productName = Objects.requireNonNull(product, "product must not be null").iosName();

    By productLabel = IosLocators.staticTextWithLabel(productName);

    return isVisible(productLabel);
  }

  @Override
  public int firstProductQuantity() {
    String quantity = waitUntilVisible(FIRST_PRODUCT_QUANTITY).getText();
    return Integer.parseInt(quantity);
  }

  @Override
  @Step("Increase the first product quantity")
  public void increaseFirstProductQuantity() {
    int expectedQuantity = firstProductQuantity() + 1;

    tap(FIRST_PRODUCT_INCREMENT);

    waitUntilTextEquals(FIRST_PRODUCT_QUANTITY, String.valueOf(expectedQuantity));
  }

  @Override
  @Step("Remove the first product from the cart")
  public void removeFirstProduct() {
    tap(FIRST_PRODUCT_REMOVE);
  }

  @Override
  public boolean isEmpty() {
    return isVisible(EMPTY_CART);
  }

  @Override
  @Step("Proceed to checkout")
  public void proceedToCheckout() {
    tap(PROCEED_TO_CHECKOUT_BUTTON);
  }
}
