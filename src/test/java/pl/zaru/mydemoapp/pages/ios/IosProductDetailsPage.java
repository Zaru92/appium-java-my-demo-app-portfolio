package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.CartPage;
import pl.zaru.mydemoapp.pages.contracts.ProductDetailsPage;
import pl.zaru.mydemoapp.testdata.model.TestProduct;

public final class IosProductDetailsPage extends BasePage implements ProductDetailsPage {

  private static final By DETAILS_SCREEN = AppiumBy.accessibilityId("ProductDetails-screen");

  private static final By ADD_TO_CART_BUTTON = AppiumBy.accessibilityId("Add To Cart");

  private static final By CART_TAB = AppiumBy.accessibilityId("Cart-tab-item");

  public IosProductDetailsPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(DETAILS_SCREEN).isDisplayed();
  }

  @Override
  public boolean displaysProduct(TestProduct product) {
    String expectedProductName =
        Objects.requireNonNull(product, "product must not be null").iosName();

    String escapedProductName = expectedProductName.replace("\\", "\\\\").replace("'", "\\'");

    By productName =
        AppiumBy.iOSNsPredicateString(
            "type == 'XCUIElementTypeStaticText' " + "AND label == '" + escapedProductName + "'");

    return waitUntilVisible(productName).isDisplayed();
  }

  @Override
  public void addToCart() {
    tap(ADD_TO_CART_BUTTON);
  }

  @Override
  public CartPage openCart() {
    tap(CART_TAB);
    return new IosCartPage(driver());
  }
}
