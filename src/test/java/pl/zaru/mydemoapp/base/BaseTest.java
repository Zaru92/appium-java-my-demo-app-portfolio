package pl.zaru.mydemoapp.base;

import io.appium.java_client.AppiumDriver;
import java.lang.reflect.Method;
import org.slf4j.MDC;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import pl.zaru.mydemoapp.config.SuiteConfigStore;
import pl.zaru.mydemoapp.config.TestConfig;
import pl.zaru.mydemoapp.driver.DriverManager;
import pl.zaru.mydemoapp.listeners.DevicePreflightListener;
import pl.zaru.mydemoapp.listeners.ScreenshotOnFailureListener;
import pl.zaru.mydemoapp.pages.ScreenFactory;

@Listeners({DevicePreflightListener.class, ScreenshotOnFailureListener.class})
public abstract class BaseTest {
  @BeforeMethod(alwaysRun = true)
  public final void setUp(ITestContext context, Method method) {
    MDC.put("testName", method.getDeclaringClass().getSimpleName() + "." + method.getName());

    boolean sessionStarted = false;

    try {
      DriverManager.startSession(SuiteConfigStore.get(context));
      sessionStarted = true;
    } finally {
      if (!sessionStarted) {
        MDC.remove("testName");
      }
    }
  }

  @AfterMethod(alwaysRun = true)
  public final void tearDown() {
    try {
      DriverManager.quitSession();
    } finally {
      MDC.remove("testName");
    }
  }

  protected final AppiumDriver driver() {
    return DriverManager.getDriver();
  }

  protected final ScreenFactory screenFactory() {
    return new ScreenFactory(driver(), DriverManager.getConfig());
  }

  protected final TestConfig testConfig() {
    return DriverManager.getConfig();
  }
}
