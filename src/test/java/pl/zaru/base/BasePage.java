package pl.zaru.page;

import io.appium.java_client.AppiumDriver;
import java.time.Duration;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {

  private static final Duration DEFAULT_WAIT_TIMEOUT = Duration.ofSeconds(10);

  private final AppiumDriver driver;
  private final WebDriverWait wait;

  protected BasePage(AppiumDriver driver) {
    this(driver, DEFAULT_WAIT_TIMEOUT);
  }

  protected BasePage(AppiumDriver driver, Duration waitTimeout) {
    this.driver = Objects.requireNonNull(driver, "driver must not be null");
    Objects.requireNonNull(waitTimeout, "waitTimeout must not be null");

    if (waitTimeout.isZero() || waitTimeout.isNegative()) {
      throw new IllegalArgumentException("waitTimeout must be positive");
    }

    wait = new WebDriverWait(driver, waitTimeout);
  }

  protected final AppiumDriver driver() {
    return driver;
  }

  protected final WebElement waitUntilVisible(By locator) {
    Objects.requireNonNull(locator, "locator must not be null");
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  protected final WebElement waitUntilClickable(By locator) {
    Objects.requireNonNull(locator, "locator must not be null");
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
  }

  protected final void tap(By locator) {
    waitUntilClickable(locator).click();
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
