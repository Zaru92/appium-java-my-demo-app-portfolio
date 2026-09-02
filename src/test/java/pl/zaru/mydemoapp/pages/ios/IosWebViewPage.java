package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.actions.IosActions;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.WebViewPage;

public final class IosWebViewPage extends BasePage implements WebViewPage {

  private static final By WEBVIEW_HEADING = AppiumBy.accessibilityId("Webview");

  private static final By URL_INPUT = AppiumBy.className("XCUIElementTypeTextField");

  private static final By GO_TO_SITE_BUTTON = AppiumBy.accessibilityId("Go To Site");

  private static final By BACK_BUTTON = AppiumBy.accessibilityId("BackButton Icons");

  private final IosActions iosActions;

  public IosWebViewPage(AppiumDriver driver) {
    super(driver);
    iosActions = new IosActions(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(WEBVIEW_HEADING).isDisplayed();
  }

  @Override
  public void openUrl(String url) {
    replaceText(URL_INPUT, url, "url");
    iosActions.hideSimulatorSoftwareKeyboardIfPresent();
    tap(GO_TO_SITE_BUTTON);
  }

  @Override
  public void returnToUrlForm() {
    tap(BACK_BUTTON);
  }
}
