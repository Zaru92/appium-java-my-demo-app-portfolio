package pl.zaru.base;

import io.appium.java_client.AppiumDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pl.zaru.config.ConfigLoader;
import pl.zaru.driver.DriverManager;

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
