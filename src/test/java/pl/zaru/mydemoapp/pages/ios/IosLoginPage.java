package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Allure;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.actions.IosActions;
import pl.zaru.mydemoapp.pages.PageContext;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.LoginPage;
import pl.zaru.mydemoapp.pages.contracts.LoginValidation;

public final class IosLoginPage extends BasePage implements LoginPage {

  private static final By LOGIN_TITLE =
      AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND label == 'Login'");

  private static final By USERNAME_INPUT = AppiumBy.className("XCUIElementTypeTextField");

  private static final By PASSWORD_INPUT = AppiumBy.className("XCUIElementTypeSecureTextField");

  private static final By LOGIN_BUTTON =
      AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeButton' AND label == 'Login'");

  private static final By USERNAME_REQUIRED_MESSAGE =
      AppiumBy.accessibilityId("Username is required");

  private static final By PASSWORD_REQUIRED_MESSAGE =
      AppiumBy.accessibilityId("Password is required");

  private final IosActions iosActions;

  public IosLoginPage(PageContext context) {
    super(context);
    iosActions = new IosActions(driver(), context.targetType());
  }

  @Override
  public boolean isLoaded() {
    return isVisible(LOGIN_TITLE);
  }

  @Override
  public void login(String username, String password) {
    Allure.step(
        "Submit login form",
        () -> {
          replaceTextAllowingEmpty(USERNAME_INPUT, username, "username");
          replaceTextAllowingEmpty(PASSWORD_INPUT, password, "password");

          iosActions.hideKeyboardIfPresent();
          tap(LOGIN_BUTTON);
          iosActions.dismissPasswordSavePromptIfPresent();
        });
  }

  @Override
  public boolean isValidationDisplayed(LoginValidation validation) {
    By locator =
        switch (Objects.requireNonNull(validation, "validation must not be null")) {
          case USERNAME_REQUIRED -> USERNAME_REQUIRED_MESSAGE;
          case PASSWORD_REQUIRED -> PASSWORD_REQUIRED_MESSAGE;
        };

    return isVisible(locator);
  }
}
