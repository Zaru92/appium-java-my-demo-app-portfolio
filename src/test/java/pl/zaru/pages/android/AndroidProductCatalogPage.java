package pl.zaru.page.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.ProductCatalogPage;
import pl.zaru.page.ProductDetailsPage;

public final class AndroidProductCatalogPage extends BasePage implements ProductCatalogPage {

  private static final String PRODUCT_NAME_ID = "com.saucelabs.mydemoapp.android:id/titleTV";

  private static final By PRODUCT_LIST = By.id("com.saucelabs.mydemoapp.android:id/productRV");

  public AndroidProductCatalogPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(PRODUCT_LIST).isDisplayed();
  }

  @Override
  public ProductDetailsPage openProduct(String productName) {
    String normalizedProductName = requireNonBlank(productName, "productName");

    By productImage =
        AppiumBy.androidUIAutomator(
            ("new UiSelector().resourceId(\"%s\").text(\"%s\")"
                    + ".fromParent(new UiSelector().resourceId(\"%s\"))")
                .formatted(PRODUCT_NAME_ID, normalizedProductName, PRODUCT_IMAGE_ID));

    tap(productImage);

    return new AndroidProductDetailsPage(driver());
  }

  private static final String PRODUCT_IMAGE_ID = "com.saucelabs.mydemoapp.android:id/productIV";
}
