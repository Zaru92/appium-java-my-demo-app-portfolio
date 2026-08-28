package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.actions.IosActions;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.LoginPage;
import pl.zaru.mydemoapp.pages.contracts.LoginValidation;

public final class IosLoginPage extends BasePage implements LoginPage {

  private static final By LOGIN_TITLE =
      AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND label == 'Login'");

  private static final By USERNAME_INPUT = AppiumBy.className("XCUIElementTypeTextField");

  private static final By PASSWORD_INPUT = AppiumBy.className("XCUIElementTypeSecureTextField");

  private static final By LOGIN_BUTTON =
      AppiumBy.iOSNsPredicateString(
          "type == 'XCUIElementTypeButton' " + "AND label == 'Login' " + "AND visible == 1");

  private static final By USERNAME_REQUIRED_MESSAGE =
      AppiumBy.accessibilityId("Username is required");

  private static final By PASSWORD_REQUIRED_MESSAGE =
      AppiumBy.accessibilityId("Password is required");

  private final IosActions iosActions;

  public IosLoginPage(AppiumDriver driver) {
    super(driver);
    iosActions = new IosActions(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(LOGIN_TITLE).isDisplayed();
  }

  @Override
  public void login(String username, String password) {
    replaceTextAllowingEmpty(USERNAME_INPUT, username, "username");
    replaceTextAllowingEmpty(PASSWORD_INPUT, password, "password");

    iosActions.hideSimulatorSoftwareKeyboardIfPresent();
    tap(LOGIN_BUTTON);
  }

  @Override
  public boolean isValidationDisplayed(LoginValidation validation) {
    By locator =
        switch (Objects.requireNonNull(validation, "validation must not be null")) {
          case USERNAME_REQUIRED -> USERNAME_REQUIRED_MESSAGE;
          case PASSWORD_REQUIRED -> PASSWORD_REQUIRED_MESSAGE;
        };

    return waitUntilVisible(locator).isDisplayed();
  }
}
