package pl.zaru.mydemoapp.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.TestNGException;
import pl.zaru.mydemoapp.config.ConfigLoader;
import pl.zaru.mydemoapp.config.TestConfig;
import pl.zaru.mydemoapp.device.DevicePreflight;

public final class DevicePreflightListener implements ISuiteListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(DevicePreflightListener.class);

  private final DevicePreflight devicePreflight = new DevicePreflight();

  @Override
  public void onStart(ISuite suite) {
    TestConfig config = ConfigLoader.load();

    LOGGER.info(
        "Running test environment preflight: platform={}, targetType={}, deviceName={}",
        config.platform().value(),
        config.device().targetType().value(),
        config.device().deviceName());

    try {
      devicePreflight.verify(config);
      LOGGER.info("Test environment preflight completed successfully.");
    } catch (RuntimeException exception) {
      throw new TestNGException(
          "Test environment preflight failed: " + exception.getMessage(), exception);
    }
  }
}
