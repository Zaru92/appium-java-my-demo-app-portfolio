package pl.zaru.mydemoapp.tests.webview;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.SkipException;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.driver.ContextManager;
import pl.zaru.mydemoapp.pages.ScreenFactory;
import pl.zaru.mydemoapp.pages.contracts.AppNavigation;
import pl.zaru.mydemoapp.pages.contracts.WebViewPage;
import pl.zaru.mydemoapp.pages.web.ExampleDomainPage;
import pl.zaru.mydemoapp.tests.TestGroups;

@Epic("My Demo App")
@Feature("Hybrid WebView")
public final class WebViewNavigationTest extends BaseTest {

  private static final String EXAMPLE_URL = "https://example.com";

  private static final String EXPECTED_HEADING = "Example Domain";

  @Story("Open a website inside the application WebView")
  @Severity(SeverityLevel.NORMAL)
  @Test(groups = {TestGroups.REGRESSION, TestGroups.WEBVIEW})
  public void shouldOpenWebsiteInsideWebViewAndReturnToNativeContext() {
    if (!(driver() instanceof AndroidDriver)) {
      throw new SkipException(
          "WebView DOM automation is unavailable in the upstream iOS 2.2.2 build "
              + "because its WKWebView is not inspectable.");
    }

    ScreenFactory screens = new ScreenFactory(driver());
    ContextManager contextManager = new ContextManager(driver());

    AppNavigation appNavigation = screens.appNavigation();
    WebViewPage webViewPage = screens.webViewPage();

    appNavigation.openWebView();

    assertTrue(webViewPage.isLoaded(), "Native WebView URL form should be displayed.");

    webViewPage.openUrl(EXAMPLE_URL);

    try {
      contextManager.switchToWebView();

      ExampleDomainPage exampleDomainPage = new ExampleDomainPage(driver());

      assertTrue(exampleDomainPage.isLoaded(), "Example Domain page should be displayed.");

      assertEquals(
          exampleDomainPage.heading(),
          EXPECTED_HEADING,
          "Unexpected heading displayed inside the WebView.");
    } finally {
      contextManager.switchToNative();
    }

    webViewPage.returnToUrlForm();

    assertTrue(
        webViewPage.isLoaded(),
        "Native WebView URL form should be displayed after returning from web content.");
  }
}
