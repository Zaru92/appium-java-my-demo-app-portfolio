package pl.zaru.mydemoapp.tests.catalog;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.pages.ScreenFactory;
import pl.zaru.mydemoapp.pages.contracts.ProductCatalogPage;
import pl.zaru.mydemoapp.tests.TestGroups;

@Epic("My Demo App")
@Feature("Catalog")
public final class AppLaunchSmokeTest extends BaseTest {

  @Story("Application launch")
  @Severity(SeverityLevel.BLOCKER)
  @Test(groups = {TestGroups.SMOKE, TestGroups.REGRESSION, TestGroups.CATALOG})
  public void shouldLaunchApplication() {

    ScreenFactory screens = new ScreenFactory(driver());

    assertNotNull(driver().getSessionId(), "Appium session should be active.");

    ProductCatalogPage catalogPage = screens.productCatalogPage();

    assertTrue(catalogPage.isLoaded(), "Product catalog should be visible after launch.");
  }
}
