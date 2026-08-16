package pl.zaru.mydemoapp.pages.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.BasePage;
import pl.zaru.mydemoapp.pages.LoginPage;

public final class AndroidLoginPage extends BasePage implements LoginPage {

  private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

  private static final By LOGIN_TITLE = AppiumBy.id(APP_PACKAGE + ":id/loginTV");

  private static final By USERNAME_INPUT = AppiumBy.id("com.saucelabs.mydemoapp.android:id/nameET");

  private static final By PASSWORD_INPUT =
      AppiumBy.id("com.saucelabs.mydemoapp.android:id/passwordET");

  private static final By LOGIN_BUTTON = AppiumBy.id("com.saucelabs.mydemoapp.android:id/loginBtn");

  public AndroidLoginPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(LOGIN_TITLE).isDisplayed();
  }

  @Override
  public void login(String username, String password) {
    replaceText(USERNAME_INPUT, username, "username");
    replaceText(PASSWORD_INPUT, password, "password");
    tap(LOGIN_BUTTON);
  }
}
