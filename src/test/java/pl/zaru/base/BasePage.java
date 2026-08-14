package pl.zaru.page;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
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

  protected void replaceText(By locator, String value, String fieldName) {
    String normalizedValue = requireNonBlank(value, fieldName);
    WebElement element = waitUntilVisible(locator);

    element.clear();
    element.sendKeys(normalizedValue);
  }

  protected void hideAndroidKeyboardIfPresent() {
    if (driver instanceof AndroidDriver androidDriver && androidDriver.isKeyboardShown()) {

      androidDriver.pressKey(new KeyEvent(AndroidKey.BACK));
    }
  }

  protected final void hideIosSimulatorSoftwareKeyboardIfPresent() {
    boolean keyboardShown = Boolean.TRUE.equals(driver.executeScript("mobile: isKeyboardShown"));

    if (!keyboardShown) {
      return;
    }

    try {
      Process process =
          new ProcessBuilder(
                  "/usr/bin/osascript",
                  "-e",
                  "tell application \"Simulator\" to activate",
                  "-e",
                  "delay 0.3",
                  "-e",
                  "tell application \"System Events\" " + "to keystroke \"k\" using command down")
              .inheritIO()
              .start();

      if (!process.waitFor(5, TimeUnit.SECONDS)) {
        process.destroyForcibly();
        throw new IllegalStateException(
            "Timed out while hiding the iOS Simulator software keyboard.");
      }

      if (process.exitValue() != 0) {
        throw new IllegalStateException(
            "Could not send Command+K to iOS Simulator. "
                + "Grant Accessibility permission to the terminal or IDE running Maven.");
      }
    } catch (IOException exception) {
      throw new IllegalStateException(
          "Could not start osascript to hide the iOS Simulator keyboard.", exception);
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException(
          "Interrupted while hiding the iOS Simulator keyboard.", exception);
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

  protected void scrollToIosElement(By locator) {
    WebElement element = driver.findElement(locator);

    driver.executeScript(
        "mobile: scrollToElement", Map.of("elementId", ((RemoteWebElement) element).getId()));
  }

  protected void scrollIosDown(By containerLocator, double distance) {
    RemoteWebElement container = (RemoteWebElement) driver.findElement(containerLocator);

    driver.executeScript(
        "mobile: scroll",
        Map.of("elementId", container.getId(), "direction", "down", "distance", distance));
  }
}
