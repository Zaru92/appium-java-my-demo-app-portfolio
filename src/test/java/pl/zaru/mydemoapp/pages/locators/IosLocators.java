package pl.zaru.mydemoapp.pages.locators;

import io.appium.java_client.AppiumBy;
import java.util.Objects;
import org.openqa.selenium.By;

public final class IosLocators {
  private IosLocators() {}

  public static By staticTextWithLabel(String label) {
    String requiredLabel = Objects.requireNonNull(label, "label must not be null");

    if (requiredLabel.isBlank()) {
      throw new IllegalArgumentException("label must not be blank");
    }

    String escapedLabel = requiredLabel.replace("\\", "\\\\").replace("'", "\\'");

    return AppiumBy.iOSNsPredicateString(
        "type == 'XCUIElementTypeStaticText' AND label == '" + escapedLabel + "'");
  }
}
