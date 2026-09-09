package pl.zaru.mydemoapp.pages.base;

import io.appium.java_client.AppiumDriver;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.zaru.mydemoapp.pages.PageContext;

public abstract class BasePage {

  private static final String LOCATOR_NULL_MESSAGE = "locator must not be null";

  private final PageContext context;
  private final WebDriverWait wait;

  protected BasePage(PageContext context) {
    this.context = Objects.requireNonNull(context, "context must not be null");
    wait = new WebDriverWait(context.driver(), context.waitTimeout());
  }

  protected final AppiumDriver driver() {
    return context.driver();
  }

  protected final PageContext pageContext() {
    return context;
  }

  protected final boolean isVisible(By locator) {
    return isVisible(locator, context.waitTimeout());
  }

  protected final boolean isVisible(By locator, Duration timeout) {
    Objects.requireNonNull(locator, LOCATOR_NULL_MESSAGE);
    Objects.requireNonNull(timeout, "timeout must not be null");

    if (timeout.isZero() || timeout.isNegative()) {
      throw new IllegalArgumentException("timeout must be positive");
    }

    try {
      new WebDriverWait(driver(), timeout)
          .until(ExpectedConditions.visibilityOfElementLocated(locator));
      return true;
    } catch (TimeoutException expected) {
      return false;
    }
  }

  protected final WebElement waitUntilVisible(By locator) {
    Objects.requireNonNull(locator, LOCATOR_NULL_MESSAGE);
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  protected final WebElement waitUntilClickable(By locator) {
    Objects.requireNonNull(locator, LOCATOR_NULL_MESSAGE);
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
  }

  protected final List<WebElement> waitUntilVisibleElements(By locator) {
    Objects.requireNonNull(locator, LOCATOR_NULL_MESSAGE);

    return wait.until(
        currentDriver -> {
          List<WebElement> visibleElements =
              currentDriver.findElements(locator).stream().filter(WebElement::isDisplayed).toList();

          return visibleElements.isEmpty() ? null : visibleElements;
        });
  }

  protected final void tap(By locator) {
    waitUntilClickable(locator).click();
  }

  protected void replaceText(By locator, String value, String fieldName) {
    replaceTextAllowingEmpty(locator, requireNonBlank(value, fieldName), fieldName);
  }

  protected void replaceTextAllowingEmpty(By locator, String value, String fieldName) {
    String normalizedFieldName = requireNonBlank(fieldName, "fieldName");
    Objects.requireNonNull(value, normalizedFieldName + " must not be null");

    WebElement element = waitUntilVisible(locator);
    element.clear();

    if (!value.isEmpty()) {
      element.sendKeys(value);
    }
  }

  protected static String requireNonBlank(String value, String fieldName) {
    Objects.requireNonNull(value, fieldName + " must not be null");

    if (value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }

    return value;
  }

  protected void waitUntilTextEquals(By locator, String expectedText) {
    wait.until(ExpectedConditions.textToBe(locator, expectedText));
  }
}
