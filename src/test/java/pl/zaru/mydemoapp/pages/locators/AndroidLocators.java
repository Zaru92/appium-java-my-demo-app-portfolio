package pl.zaru.mydemoapp.pages.locators;

import io.appium.java_client.AppiumBy;
import java.util.Objects;
import org.openqa.selenium.By;

public final class AndroidLocators {
  private AndroidLocators() {}

  public static By byResourceIdAndText(String resourceId, String text) {
    return AppiumBy.androidUIAutomator(
        "new UiSelector().resourceId(\"%s\").text(\"%s\")"
            .formatted(escape(resourceId, "resourceId"), escape(text, "text")));
  }

  public static By siblingByResourceId(
      String sourceResourceId, String sourceText, String siblingResourceId) {
    return AppiumBy.androidUIAutomator(
        ("new UiSelector().resourceId(\"%s\").text(\"%s\")"
                + ".fromParent(new UiSelector().resourceId(\"%s\"))")
            .formatted(
                escape(sourceResourceId, "sourceResourceId"),
                escape(sourceText, "sourceText"),
                escape(siblingResourceId, "siblingResourceId")));
  }

  private static String escape(String value, String fieldName) {
    String requiredValue = Objects.requireNonNull(value, fieldName + " must not be null");

    if (requiredValue.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }

    return requiredValue.replace("\\", "\\\\").replace("\"", "\\\"");
  }
}
