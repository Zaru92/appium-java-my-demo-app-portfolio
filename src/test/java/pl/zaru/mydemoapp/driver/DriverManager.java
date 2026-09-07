package pl.zaru.mydemoapp.driver;

import io.appium.java_client.AppiumDriver;
import java.util.Objects;
import pl.zaru.mydemoapp.config.TestConfig;

public final class DriverManager {
  private static final ThreadLocal<Session> SESSION = new ThreadLocal<>();

  private DriverManager() {}

  public static void startSession(TestConfig config) {
    Objects.requireNonNull(config, "config must not be null");

    if (hasSession()) {
      throw new IllegalStateException("An Appium session is already active on the current thread.");
    }

    AppiumDriver driver = DriverFactory.create(config);
    SESSION.set(new Session(driver, config));
  }

  public static AppiumDriver getDriver() {
    Session session = SESSION.get();

    if (session == null) {
      throw new IllegalStateException("No Appium driver is bound to the current thread.");
    }

    return session.driver();
  }

  public static TestConfig getConfig() {
    Session session = SESSION.get();

    if (session == null) {
      throw new IllegalStateException("No test configuration is bound to the current thread.");
    }

    return session.config();
  }

  public static boolean hasSession() {
    return SESSION.get() != null;
  }

  public static void quitSession() {
    Session session = SESSION.get();

    try {
      if (session != null) {
        session.driver().quit();
      }
    } finally {
      SESSION.remove();
    }
  }

  private record Session(AppiumDriver driver, TestConfig config) {
    private Session {
      Objects.requireNonNull(driver, "driver must not be null");
      Objects.requireNonNull(config, "config must not be null");
    }
  }
}
