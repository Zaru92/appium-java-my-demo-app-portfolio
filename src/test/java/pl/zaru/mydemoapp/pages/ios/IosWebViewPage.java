package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.actions.IosActions;
import pl.zaru.mydemoapp.pages.PageContext;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.WebViewPage;

public final class IosWebViewPage extends BasePage implements WebViewPage {

  private static final By WEBVIEW_HEADING = AppiumBy.accessibilityId("Webview");

  private static final By URL_INPUT = AppiumBy.className("XCUIElementTypeTextField");

  private static final By GO_TO_SITE_BUTTON = AppiumBy.accessibilityId("Go To Site");

  private static final By BACK_BUTTON = AppiumBy.accessibilityId("BackButton Icons");

  private final IosActions iosActions;

  public IosWebViewPage(PageContext context) {
    super(context);
    iosActions = new IosActions(driver(), context.targetType());
  }

  @Override
  public boolean isLoaded() {
    return isVisible(WEBVIEW_HEADING);
  }

  @Override
  @Step("Open URL in the embedded WebView")
  public void openUrl(String url) {
    replaceText(URL_INPUT, url, "url");
    iosActions.hideKeyboardIfPresent();
    tap(GO_TO_SITE_BUTTON);
  }

  @Override
  @Step("Return to the WebView URL form")
  public void returnToUrlForm() {
    tap(BACK_BUTTON);
  }
}
