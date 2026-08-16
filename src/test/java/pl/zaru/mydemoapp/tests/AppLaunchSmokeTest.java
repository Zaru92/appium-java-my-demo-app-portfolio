package pl.zaru.mydemoapp.tests;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.pages.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.ProductCatalogPageFactory;

public final class AppLaunchSmokeTest extends BaseTest {

  @Test(groups = "smoke")
  public void shouldLaunchApplication() {
    assertNotNull(driver().getSessionId(), "Appium session should be active.");

    ProductCatalogPage catalogPage = ProductCatalogPageFactory.create(driver());

    assertTrue(catalogPage.isLoaded(), "Product catalog should be visible after launch.");
  }
}
