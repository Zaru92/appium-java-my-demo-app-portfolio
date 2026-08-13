package pl.zaru.tests;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;
import pl.zaru.base.BaseTest;

public final class AppLaunchSmokeTest extends BaseTest {
  @Test(groups = "smoke")
  public void shouldLaunchApplication() {
    assertNotNull(driver().getSessionId(), "Appium session should be active.");

    assertFalse(driver().getPageSource().isBlank(), "Application page source should not be blank.");
  }
}
