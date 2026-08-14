package pl.zaru.page.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.ProductDetailsPage;

public final class IosProductDetailsPage extends BasePage implements ProductDetailsPage {

  private static final By DETAILS_SCREEN = AppiumBy.accessibilityId("ProductDetails-screen");

  private final By productName;

  public IosProductDetailsPage(AppiumDriver driver, String expectedProductName) {
    super(driver);

    String normalizedProductName = requireNonBlank(expectedProductName, "expectedProductName");

    productName =
        AppiumBy.iOSNsPredicateString(
            "type == 'XCUIElementTypeStaticText' AND label == '%s'"
                .formatted(normalizedProductName));
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(DETAILS_SCREEN).isDisplayed();
  }

  @Override
  public String displayedProductName() {
    return waitUntilVisible(productName).getText();
  }
}
