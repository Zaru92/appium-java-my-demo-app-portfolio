package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.AppNavigation;

public final class IosAppNavigation extends BasePage implements AppNavigation {

  private static final By MORE_TAB = AppiumBy.accessibilityId("More-tab-item");

  private static final By WEBVIEW_MENU_ITEM = AppiumBy.accessibilityId("Webview-menu-item");

  public IosAppNavigation(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public void openWebView() {
    tap(MORE_TAB);
    tap(WEBVIEW_MENU_ITEM);
  }
}
