package pl.zaru.driver;

import io.appium.java_client.AppiumDriver;
import java.util.Objects;
import pl.zaru.config.TestConfig;

public final class DriverManager {
  private static final ThreadLocal<AppiumDriver> DRIVER = new ThreadLocal<>();

  private DriverManager() {}

  public static void startSession(TestConfig config) {
    Objects.requireNonNull(config, "config must not be null");

    if (hasSession()) {
      throw new IllegalStateException("An Appium session is already active on the current thread.");
    }

    AppiumDriver driver = DriverFactory.create(config);
    DRIVER.set(driver);
  }

  public static AppiumDriver getDriver() {
    AppiumDriver driver = DRIVER.get();

    if (driver == null) {
      throw new IllegalStateException("No Appium driver is bound to the current thread.");
    }

    return driver;
  }

  public static boolean hasSession() {
    return DRIVER.get() != null;
  }

  public static void quitSession() {
    AppiumDriver driver = DRIVER.get();

    try {
      if (driver != null) {
        driver.quit();
      }
    } finally {
      DRIVER.remove();
    }
  }
}
