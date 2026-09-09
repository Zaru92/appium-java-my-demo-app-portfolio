package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.zaru.mydemoapp.pages.PageContext;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.locators.IosLocators;

final class IosValidationAlert extends BasePage {

  private static final By VALIDATION_ALERT =
      AppiumBy.iOSNsPredicateString(
          "type == 'XCUIElementTypeAlert' AND name == 'Validation Error!'");

  IosValidationAlert(PageContext context) {
    super(context);
  }

  boolean isDisplayed(String message) {
    return isVisible(VALIDATION_ALERT) && isVisible(IosLocators.staticTextWithLabel(message));
  }

  void dismissIfPresent() {
    if (driver().findElements(VALIDATION_ALERT).stream().noneMatch(WebElement::isDisplayed)) {
      return;
    }

    WebDriverWait alertWait = new WebDriverWait(driver(), pageContext().waitTimeout());
    alertWait.until(ExpectedConditions.alertIsPresent()).accept();
    alertWait.until(ExpectedConditions.invisibilityOfElementLocated(VALIDATION_ALERT));
  }
}
