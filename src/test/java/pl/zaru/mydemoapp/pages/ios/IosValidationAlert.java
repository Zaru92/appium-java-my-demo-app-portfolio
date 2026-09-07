package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.locators.IosLocators;

final class IosValidationAlert extends BasePage {

  private static final Duration ALERT_TIMEOUT = Duration.ofSeconds(10);

  private static final By VALIDATION_ALERT =
      AppiumBy.iOSNsPredicateString(
          "type == 'XCUIElementTypeAlert' AND name == 'Validation Error!'");

  IosValidationAlert(AppiumDriver driver) {
    super(driver, ALERT_TIMEOUT);
  }

  boolean isDisplayed(String message) {
    waitUntilVisible(VALIDATION_ALERT);
    return waitUntilVisible(IosLocators.staticTextWithLabel(message)).isDisplayed();
  }

  void dismissIfPresent() {
    if (driver().findElements(VALIDATION_ALERT).stream().noneMatch(WebElement::isDisplayed)) {
      return;
    }

    WebDriverWait alertWait = new WebDriverWait(driver(), ALERT_TIMEOUT);
    alertWait.until(ExpectedConditions.alertIsPresent()).accept();
    alertWait.until(ExpectedConditions.invisibilityOfElementLocated(VALIDATION_ALERT));
  }
}
