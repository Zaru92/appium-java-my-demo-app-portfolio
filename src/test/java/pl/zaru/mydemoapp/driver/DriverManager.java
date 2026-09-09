package pl.zaru.mydemoapp.driver;

import io.appium.java_client.AppiumDriver;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.zaru.mydemoapp.config.TestConfig;

public final class DriverManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(DriverManager.class);

  private static final ThreadLocal<Session> SESSION = new ThreadLocal<>();

  private DriverManager() {}

  public static void startSession(TestConfig config) {
    Objects.requireNonNull(config, "config must not be null");

    if (hasSession()) {
      throw new IllegalStateException("An Appium session is already active on the current thread.");
    }

    LOGGER.info(
        "Starting Appium session: platform={}, targetType={}, deviceName={}",
        config.platform().value(),
        config.device().targetType().value(),
        config.device().deviceName());

    AppiumDriver driver = DriverFactory.create(config);
    SESSION.set(new Session(driver, config));

    LOGGER.info("Appium session started: sessionId={}", driver.getSessionId());
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
        LOGGER.info("Stopping Appium session: sessionId={}", session.driver().getSessionId());
        session.driver().quit();
        LOGGER.info("Appium session stopped");
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
