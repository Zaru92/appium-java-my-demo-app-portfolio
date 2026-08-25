package pl.zaru.mydemoapp.base;

import io.appium.java_client.AppiumDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import pl.zaru.mydemoapp.config.ConfigLoader;
import pl.zaru.mydemoapp.driver.DriverManager;
import pl.zaru.mydemoapp.listeners.DevicePreflightListener;
import pl.zaru.mydemoapp.listeners.ScreenshotOnFailureListener;

@Listeners({DevicePreflightListener.class, ScreenshotOnFailureListener.class})
public abstract class BaseTest {
  @BeforeMethod(alwaysRun = true)
  public final void setUp() {
    DriverManager.startSession(ConfigLoader.load());
  }

  @AfterMethod(alwaysRun = true)
  public final void tearDown() {
    DriverManager.quitSession();
  }

  protected final AppiumDriver driver() {
    return DriverManager.getDriver();
  }
}
