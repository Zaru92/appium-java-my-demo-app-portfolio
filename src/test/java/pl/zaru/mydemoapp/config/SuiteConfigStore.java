package pl.zaru.mydemoapp.config;

import java.util.Map;
import java.util.Objects;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.xml.XmlTest;

public final class SuiteConfigStore {

  private static final String ATTRIBUTE_NAME = SuiteConfigStore.class.getName() + ".configurations";

  private SuiteConfigStore() {}

  public static void store(ISuite suite, Map<String, TestConfig> configurations) {
    Objects.requireNonNull(suite, "suite must not be null");
    Objects.requireNonNull(configurations, "configurations must not be null");

    if (configurations.isEmpty()) {
      throw new IllegalArgumentException("configurations must not be empty");
    }

    suite.setAttribute(ATTRIBUTE_NAME, Map.copyOf(configurations));
  }

  public static TestConfig get(ITestContext context) {
    Objects.requireNonNull(context, "context must not be null");

    Object storedConfigurations = context.getSuite().getAttribute(ATTRIBUTE_NAME);

    if (!(storedConfigurations instanceof Map<?, ?> configurations)) {
      throw new IllegalStateException(
          "Validated test configuration is not available in the suite.");
    }

    XmlTest xmlTest = context.getCurrentXmlTest();
    String testName = xmlTest == null ? context.getSuite().getName() : xmlTest.getName();
    Object configuration = configurations.get(testName);

    if (!(configuration instanceof TestConfig testConfig)) {
      throw new IllegalStateException(
          "Validated test configuration is not available for TestNG test: " + testName);
    }

    return testConfig;
  }

  public static void clear(ISuite suite) {
    Objects.requireNonNull(suite, "suite must not be null").removeAttribute(ATTRIBUTE_NAME);
  }
}
