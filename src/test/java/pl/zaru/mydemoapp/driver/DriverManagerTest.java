package pl.zaru.mydemoapp.driver;

import static org.testng.Assert.assertFalse;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public final class DriverManagerTest {
  @AfterMethod(alwaysRun = true)
  public void cleanUp() {
    DriverManager.quitSession();
  }

  @Test(
      expectedExceptions = IllegalStateException.class,
      expectedExceptionsMessageRegExp = "No Appium driver is bound to the current thread\\.")
  public void shouldRejectAccessBeforeSessionStarts() {
    DriverManager.getDriver();
  }

  @Test(
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "config must not be null")
  public void shouldRejectNullConfiguration() {
    DriverManager.startSession(null);
  }

  @Test
  public void shouldAllowCleanupWithoutActiveSession() {
    DriverManager.quitSession();

    assertFalse(DriverManager.hasSession());
  }
}
