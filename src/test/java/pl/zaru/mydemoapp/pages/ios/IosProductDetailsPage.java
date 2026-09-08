package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.PageContext;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.CartPage;
import pl.zaru.mydemoapp.pages.contracts.ProductDetailsPage;
import pl.zaru.mydemoapp.pages.locators.IosLocators;
import pl.zaru.mydemoapp.testdata.model.TestProduct;

public final class IosProductDetailsPage extends BasePage implements ProductDetailsPage {

  private static final By DETAILS_SCREEN = AppiumBy.accessibilityId("ProductDetails-screen");

  private static final By ADD_TO_CART_BUTTON = AppiumBy.accessibilityId("Add To Cart");

  private static final By CART_TAB = AppiumBy.accessibilityId("Cart-tab-item");

  public IosProductDetailsPage(PageContext context) {
    super(context);
  }

  @Override
  public boolean isLoaded() {
    return isVisible(DETAILS_SCREEN);
  }

  @Override
  public boolean displaysProduct(TestProduct product) {
    String expectedProductName =
        Objects.requireNonNull(product, "product must not be null").iosName();

    By productName = IosLocators.staticTextWithLabel(expectedProductName);

    return isVisible(productName);
  }

  @Override
  @Step("Add product to cart")
  public void addToCart() {
    tap(ADD_TO_CART_BUTTON);
  }

  @Override
  @Step("Open cart")
  public CartPage openCart() {
    tap(CART_TAB);
    return new IosCartPage(pageContext());
  }
}
