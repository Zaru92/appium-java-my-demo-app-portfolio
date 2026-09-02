package pl.zaru.mydemoapp.pages.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.actions.AndroidActions;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.WebViewPage;

public final class AndroidWebViewPage extends BasePage implements WebViewPage {

  private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

  private static final By WEBVIEW_HEADING = AppiumBy.id(APP_PACKAGE + ":id/webViewTV");

  private static final By URL_INPUT = AppiumBy.id(APP_PACKAGE + ":id/urlET");

  private static final By GO_TO_SITE_BUTTON = AppiumBy.id(APP_PACKAGE + ":id/goBtn");

  private final AndroidActions androidActions;

  public AndroidWebViewPage(AppiumDriver driver) {
    super(driver);
    androidActions = new AndroidActions(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(WEBVIEW_HEADING).isDisplayed();
  }

  @Override
  public void openUrl(String url) {
    replaceText(URL_INPUT, url, "url");
    androidActions.hideKeyboardIfPresent();
    tap(GO_TO_SITE_BUTTON);
  }

  @Override
  public void returnToUrlForm() {
    driver().navigate().back();
  }
}
