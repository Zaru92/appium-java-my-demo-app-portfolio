package pl.zaru.mydemoapp.actions;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

public final class IosActions {
  private static final Duration KEYBOARD_HIDE_TIMEOUT = Duration.ofSeconds(5);

  private final IOSDriver driver;

  public IosActions(AppiumDriver driver) {
    Objects.requireNonNull(driver, "driver must not be null");

    if (!(driver instanceof IOSDriver iosDriver)) {
      throw new IllegalArgumentException("IosActions requires an IOSDriver.");
    }

    this.driver = iosDriver;
  }

  public void scrollTo(By locator) {
    Objects.requireNonNull(locator, "locator must not be null");

    WebElement element = driver.findElement(locator);

    if (!(element instanceof RemoteWebElement remoteElement)) {
      throw new IllegalStateException("iOS element does not expose a remote element ID.");
    }

    driver.executeScript("mobile: scrollToElement", Map.of("elementId", remoteElement.getId()));
  }

  public void hideSimulatorSoftwareKeyboardIfPresent() {
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
}
