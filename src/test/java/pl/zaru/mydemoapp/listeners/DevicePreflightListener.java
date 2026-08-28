package pl.zaru.mydemoapp.listeners;

import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.TestNGException;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import pl.zaru.mydemoapp.config.ConfigLoader;
import pl.zaru.mydemoapp.config.TestConfig;
import pl.zaru.mydemoapp.device.DevicePreflight;
import pl.zaru.mydemoapp.device.ParallelConfigValidator;
import pl.zaru.mydemoapp.reporting.AllureEnvironment;

public final class DevicePreflightListener implements ISuiteListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(DevicePreflightListener.class);

  private final DevicePreflight devicePreflight = new DevicePreflight();

  private final ParallelConfigValidator parallelConfigValidator = new ParallelConfigValidator();

  @Override
  @SuppressWarnings("PMD.AvoidCatchingGenericException")
  public void onStart(ISuite suite) {
    try {
      Map<String, TestConfig> configs = loadConfigurations(suite);

      boolean parallel = verifyParallelConfiguration(suite, configs);

      for (Map.Entry<String, TestConfig> entry : configs.entrySet()) {

        String testName = entry.getKey();
        TestConfig config = entry.getValue();

        LOGGER.info(
            "Running test environment preflight: test={}, platform={}, targetType={}, deviceName={}",
            testName,
            config.platform().value(),
            config.device().targetType().value(),
            config.device().deviceName());

        devicePreflight.verify(config);
      }

      AllureEnvironment.write(configs, parallel);

      LOGGER.info(
          "Test environment preflight completed successfully for {} configuration(s).",
          configs.size());

    } catch (RuntimeException exception) {
      throw new TestNGException(
          "Test environment preflight failed: " + exception.getMessage(), exception);
    }
  }

  private static Map<String, TestConfig> loadConfigurations(ISuite suite) {

    Map<String, TestConfig> configs = new LinkedHashMap<>();

    for (XmlTest xmlTest : suite.getXmlSuite().getTests()) {

      String testName = xmlTest.getName();

      if (configs.containsKey(testName)) {
        throw new IllegalStateException("Duplicate TestNG test name: " + testName);
      }

      configs.put(testName, ConfigLoader.load(xmlTest.getAllParameters()));
    }

    if (configs.isEmpty()) {
      configs.put(suite.getName(), ConfigLoader.load());
    }

    return configs;
  }

  private boolean verifyParallelConfiguration(ISuite suite, Map<String, TestConfig> configs) {

    XmlSuite.ParallelMode parallelMode = suite.getXmlSuite().getParallel();

    if (parallelMode == null || parallelMode == XmlSuite.ParallelMode.NONE) {

      return false;
    }

    if (parallelMode != XmlSuite.ParallelMode.TESTS) {

      throw new IllegalStateException(
          "Mobile parallel execution supports only TestNG parallel=\"tests\", but was: "
              + parallelMode);
    }

    parallelConfigValidator.verify(configs);
    return true;
  }
}
