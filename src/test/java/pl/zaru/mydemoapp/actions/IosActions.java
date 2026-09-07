package pl.zaru.mydemoapp.actions;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.zaru.mydemoapp.config.Platform;
import pl.zaru.mydemoapp.config.TargetType;
import pl.zaru.mydemoapp.system.CommandExecutor;
import pl.zaru.mydemoapp.system.CommandResult;
import pl.zaru.mydemoapp.system.SystemCommandExecutor;

public final class IosActions {
  private static final Duration KEYBOARD_HIDE_TIMEOUT = Duration.ofSeconds(5);

  private static final Duration OPTIONAL_SYSTEM_PROMPT_TIMEOUT = Duration.ofSeconds(3);

  private static final Duration SYSTEM_PROMPT_DISMISS_TIMEOUT = Duration.ofSeconds(10);

  private static final By SAVE_PASSWORD_PROMPT =
      AppiumBy.iOSNsPredicateString(
          "type == 'XCUIElementTypeSheet' AND name == 'Save Password?' AND visible == 1");

  private static final List<String> HIDE_SIMULATOR_KEYBOARD_COMMAND =
      List.of(
          "/usr/bin/osascript",
          "-e",
          "tell application \"Simulator\" to activate",
          "-e",
          "delay 0.3",
          "-e",
          "tell application \"System Events\" to keystroke \"k\" using command down");

  private final IOSDriver driver;

  private final TargetType targetType;

  private final CommandExecutor commandExecutor;

  public IosActions(AppiumDriver driver, TargetType targetType) {
    this(driver, targetType, new SystemCommandExecutor());
  }

  IosActions(AppiumDriver driver, TargetType targetType, CommandExecutor commandExecutor) {
    Objects.requireNonNull(driver, "driver must not be null");

    if (!(driver instanceof IOSDriver iosDriver)) {
      throw new IllegalArgumentException("IosActions requires an IOSDriver.");
    }

    this.driver = iosDriver;
    this.targetType = Objects.requireNonNull(targetType, "targetType must not be null");
    this.commandExecutor =
        Objects.requireNonNull(commandExecutor, "commandExecutor must not be null");

    if (!targetType.supports(Platform.IOS)) {
      throw new IllegalArgumentException("IosActions requires an iOS target type.");
    }
  }

  public void scrollTo(By locator) {
    Objects.requireNonNull(locator, "locator must not be null");

    WebElement element = driver.findElement(locator);

    if (!(element instanceof RemoteWebElement remoteElement)) {
      throw new IllegalStateException("iOS element does not expose a remote element ID.");
    }

    driver.executeScript("mobile: scrollToElement", Map.of("elementId", remoteElement.getId()));
  }

  public void hideKeyboardIfPresent() {
    boolean keyboardShown = Boolean.TRUE.equals(driver.executeScript("mobile: isKeyboardShown"));

    if (!keyboardShown) {
      return;
    }

    if (targetType == TargetType.REAL_DEVICE) {
      driver.hideKeyboard();
      return;
    }

    hideSimulatorSoftwareKeyboard();
  }

  public void dismissPasswordSavePromptIfPresent() {
    WebDriverWait promptWait = new WebDriverWait(driver, OPTIONAL_SYSTEM_PROMPT_TIMEOUT);

    try {
      promptWait.until(ExpectedConditions.visibilityOfElementLocated(SAVE_PASSWORD_PROMPT));
    } catch (TimeoutException ignored) {
      return;
    }

    By notNowButtonLocator =
        AppiumBy.iOSClassChain(
            "**/XCUIElementTypeSheet[`name == 'Save Password?' AND visible == 1`]"
                + "/**/XCUIElementTypeButton[`name == 'Not Now' AND visible == 1`]");

    try {
      WebDriverWait dismissWait = new WebDriverWait(driver, SYSTEM_PROMPT_DISMISS_TIMEOUT);

      WebElement notNowButton =
          dismissWait.until(ExpectedConditions.elementToBeClickable(notNowButtonLocator));

      Rectangle bounds = notNowButton.getRect();

      if (bounds.getWidth() <= 0 || bounds.getHeight() <= 0) {
        throw new IllegalStateException(
            "The iOS Save Password prompt's Not Now button has invalid bounds: " + bounds);
      }

      double centerX = bounds.getX() + bounds.getWidth() / 2.0;
      double centerY = bounds.getY() + bounds.getHeight() / 2.0;

      driver.executeScript("mobile: tap", Map.of("x", centerX, "y", centerY));

      dismissWait.until(ExpectedConditions.invisibilityOfElementLocated(SAVE_PASSWORD_PROMPT));
    } catch (TimeoutException exception) {
      throw new IllegalStateException(
          "The iOS Save Password prompt was detected but could not be dismissed by tapping Not Now.",
          exception);
    }
  }

  private void hideSimulatorSoftwareKeyboard() {
    CommandResult result =
        commandExecutor.execute(HIDE_SIMULATOR_KEYBOARD_COMMAND, KEYBOARD_HIDE_TIMEOUT);

    if (!result.successful()) {
      throw new IllegalStateException(
          "Could not send Command+K to iOS Simulator. "
              + "Grant Accessibility permission to the terminal or IDE running Maven. Output: "
              + result.displayOutput());
    }
  }
}
