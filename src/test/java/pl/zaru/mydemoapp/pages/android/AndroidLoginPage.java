package pl.zaru.mydemoapp.pages.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.LoginPage;
import pl.zaru.mydemoapp.pages.contracts.LoginValidation;

public final class AndroidLoginPage extends BasePage implements LoginPage {

  private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

  private static final By LOGIN_TITLE = AppiumBy.id(APP_PACKAGE + ":id/loginTV");

  private static final By USERNAME_INPUT = AppiumBy.id("com.saucelabs.mydemoapp.android:id/nameET");

  private static final By PASSWORD_INPUT =
      AppiumBy.id("com.saucelabs.mydemoapp.android:id/passwordET");

  private static final By LOGIN_BUTTON = AppiumBy.id("com.saucelabs.mydemoapp.android:id/loginBtn");

  private static final By USERNAME_REQUIRED_MESSAGE = AppiumBy.id(APP_PACKAGE + ":id/nameErrorTV");

  private static final By PASSWORD_REQUIRED_MESSAGE =
      AppiumBy.id(APP_PACKAGE + ":id/passwordErrorTV");

  public AndroidLoginPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(LOGIN_TITLE).isDisplayed();
  }

  @Override
  public void login(String username, String password) {
    replaceTextAllowingEmpty(USERNAME_INPUT, username, "username");
    replaceTextAllowingEmpty(PASSWORD_INPUT, password, "password");
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
