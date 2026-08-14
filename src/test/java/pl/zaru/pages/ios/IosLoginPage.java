package pl.zaru.page.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.LoginPage;

public final class IosLoginPage extends BasePage implements LoginPage {

  private static final By LOGIN_TITLE =
      AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND label == 'Login'");

  private static final By USERNAME_INPUT = AppiumBy.className("XCUIElementTypeTextField");

  private static final By PASSWORD_INPUT = AppiumBy.className("XCUIElementTypeSecureTextField");

  private static final By LOGIN_BUTTON =
      AppiumBy.iOSNsPredicateString(
          "type == 'XCUIElementTypeButton' " + "AND label == 'Login' " + "AND visible == 1");

  private static final String PREDEFINED_USER_PASSWORD = "10203040";

  public IosLoginPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(LOGIN_TITLE).isDisplayed();
  }

  @Override
  public void login(String username, String password) {
    String normalizedUsername = requireNonBlank(username, "username");
    String normalizedPassword = requireNonBlank(password, "password");

    if (!PREDEFINED_USER_PASSWORD.equals(normalizedPassword)) {
      throw new IllegalArgumentException(
          "iOS predefined users require password: " + PREDEFINED_USER_PASSWORD);
    }

    tap(AppiumBy.accessibilityId(normalizedUsername));
    tap(LOGIN_BUTTON);
  }
}
