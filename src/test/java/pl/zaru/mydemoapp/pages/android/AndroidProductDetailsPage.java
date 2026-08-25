package pl.zaru.mydemoapp.pages.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.CartPage;
import pl.zaru.mydemoapp.pages.contracts.ProductDetailsPage;
import pl.zaru.mydemoapp.testdata.model.TestProduct;

public final class AndroidProductDetailsPage extends BasePage implements ProductDetailsPage {

  private static final By PRODUCT_NAME = By.id("com.saucelabs.mydemoapp.android:id/productTV");

  private static final By ADD_TO_CART_BUTTON = By.id("com.saucelabs.mydemoapp.android:id/cartBt");

  private static final By CART_BUTTON = AppiumBy.id("com.saucelabs.mydemoapp.android:id/cartRL");

  public AndroidProductDetailsPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(ADD_TO_CART_BUTTON).isDisplayed();
  }

  @Override
  public boolean displaysProduct(TestProduct product) {
    String expectedProductName =
        Objects.requireNonNull(product, "product must not be null").androidName();

    String displayedProductName = waitUntilVisible(PRODUCT_NAME).getText();

    return expectedProductName.equals(displayedProductName);
  }

  @Override
  public void addToCart() {
    tap(ADD_TO_CART_BUTTON);
  }

  @Override
  public CartPage openCart() {
    tap(CART_BUTTON);
    return new AndroidCartPage(driver());
  }
}
