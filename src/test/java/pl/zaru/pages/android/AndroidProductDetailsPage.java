package pl.zaru.page.android;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.ProductDetailsPage;

public final class AndroidProductDetailsPage extends BasePage implements ProductDetailsPage {

  private static final By PRODUCT_NAME = By.id("com.saucelabs.mydemoapp.android:id/productTV");

  private static final By ADD_TO_CART_BUTTON = By.id("com.saucelabs.mydemoapp.android:id/cartBt");

  public AndroidProductDetailsPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(ADD_TO_CART_BUTTON).isDisplayed();
  }

  @Override
  public String displayedProductName() {
    return waitUntilVisible(PRODUCT_NAME).getText();
  }
}
