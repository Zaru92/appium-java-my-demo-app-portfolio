package pl.zaru.mydemoapp.tests.catalog;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.pages.ScreenFactory;
import pl.zaru.mydemoapp.pages.contracts.ProductCatalogPage;

public final class AppLaunchSmokeTest extends BaseTest {

  @Test(groups = "smoke")
  public void shouldLaunchApplication() {

    ScreenFactory screens = new ScreenFactory(driver());

    assertNotNull(driver().getSessionId(), "Appium session should be active.");

    ProductCatalogPage catalogPage = screens.productCatalogPage();

    assertTrue(catalogPage.isLoaded(), "Product catalog should be visible after launch.");
  }
}
