package pl.zaru.mydemoapp.pages.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.AppNavigation;

public final class AndroidAppNavigation extends BasePage implements AppNavigation {

  private static final By MENU_BUTTON = AppiumBy.accessibilityId("View menu");

  private static final By WEBVIEW_MENU_ITEM =
      AppiumBy.androidUIAutomator(
          "new UiSelector()"
              + ".resourceId(\"com.saucelabs.mydemoapp.android:id/itemTV\")"
              + ".text(\"WebView\")");

  public AndroidAppNavigation(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public void openWebView() {
    tap(MENU_BUTTON);
    tap(WEBVIEW_MENU_ITEM);
  }
}
