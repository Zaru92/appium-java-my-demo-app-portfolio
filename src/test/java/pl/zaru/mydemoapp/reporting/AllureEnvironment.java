package pl.zaru.mydemoapp.reporting;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.zaru.mydemoapp.config.TestConfig;

public final class AllureEnvironment {
  private static final Logger LOGGER = LoggerFactory.getLogger(AllureEnvironment.class);

  private static final Path RESULTS_DIRECTORY = Path.of("target", "allure-results");

  private static final String ENVIRONMENT_FILE = "environment.properties";

  private AllureEnvironment() {}

  public static void write(TestConfig config) {
    try {
      write(config, RESULTS_DIRECTORY);
    } catch (IOException exception) {
      LOGGER.warn("Could not write Allure environment information.", exception);
    }
  }

  public static void write(Map<String, TestConfig> configs, boolean parallel) {

    try {
      write(configs, parallel, RESULTS_DIRECTORY);
    } catch (IOException exception) {
      LOGGER.warn("Could not write Allure environment information.", exception);
    }
  }

  static void write(TestConfig config, Path resultsDirectory) throws IOException {

    Objects.requireNonNull(config, "config must not be null");

    Objects.requireNonNull(resultsDirectory, "resultsDirectory must not be null");

    Properties environment = commonEnvironment();

    addSingleTarget(environment, config);
    save(environment, resultsDirectory);
  }

  static void write(Map<String, TestConfig> configs, boolean parallel, Path resultsDirectory)
      throws IOException {

    Objects.requireNonNull(configs, "configs must not be null");

    Objects.requireNonNull(resultsDirectory, "resultsDirectory must not be null");

    if (configs.isEmpty()) {
      throw new IllegalArgumentException("configs must not be empty");
    }

    if (configs.size() == 1) {
      write(configs.values().iterator().next(), resultsDirectory);
      return;
    }

    Properties environment = commonEnvironment();

    environment.setProperty("execution.mode", parallel ? "parallel" : "sequential");

    environment.setProperty("target.count", String.valueOf(configs.size()));

    int index = 1;

    for (Map.Entry<String, TestConfig> entry : configs.entrySet()) {

      String testName = Objects.requireNonNull(entry.getKey(), "test name must not be null");

      TestConfig config = Objects.requireNonNull(entry.getValue(), "test config must not be null");

      addParallelTarget(environment, index, testName, config);

      index++;
    }

    save(environment, resultsDirectory);
  }

  private static Properties commonEnvironment() {
    Properties environment = new Properties();

    environment.setProperty("java.version", System.getProperty("java.version", "unknown"));

    environment.setProperty("os.name", System.getProperty("os.name", "unknown"));

    environment.setProperty("os.version", System.getProperty("os.version", "unknown"));

    environment.setProperty("os.architecture", System.getProperty("os.arch", "unknown"));

    return environment;
  }

  private static void addSingleTarget(Properties environment, TestConfig config) {

    environment.setProperty("platform", config.platform().value());

    environment.setProperty("target.type", config.device().targetType().value());

    environment.setProperty("device.name", config.device().deviceName());

    environment.setProperty(
        "platform.version", config.device().platformVersion().orElse("not specified"));

    environment.setProperty("automation.name", config.automationName());

    environment.setProperty("application", config.appPath().getFileName().toString());

    environment.setProperty("appium.server", safeServerAddress(config.appiumUrl()));

    environment.setProperty(
        "new.command.timeout.seconds", String.valueOf(config.newCommandTimeout().toSeconds()));

    config
        .device()
        .systemPort()
        .ifPresent(port -> environment.setProperty("system.port", String.valueOf(port)));

    config
        .device()
        .wdaLocalPort()
        .ifPresent(port -> environment.setProperty("wda.local.port", String.valueOf(port)));
  }

  private static void addParallelTarget(
      Properties environment, int index, String testName, TestConfig config) {

    String prefix = "target." + index + ".";

    environment.setProperty(prefix + "test.name", testName);

    environment.setProperty(prefix + "platform", config.platform().value());

    environment.setProperty(prefix + "target.type", config.device().targetType().value());

    environment.setProperty(prefix + "device.name", config.device().deviceName());

    environment.setProperty(
        prefix + "platform.version", config.device().platformVersion().orElse("not specified"));

    environment.setProperty(prefix + "automation.name", config.automationName());

    environment.setProperty(prefix + "application", config.appPath().getFileName().toString());

    environment.setProperty(prefix + "appium.server", safeServerAddress(config.appiumUrl()));

    environment.setProperty(
        prefix + "new.command.timeout.seconds",
        String.valueOf(config.newCommandTimeout().toSeconds()));

    config.device().udid().ifPresent(udid -> environment.setProperty(prefix + "device.udid", udid));

    config
        .device()
        .systemPort()
        .ifPresent(port -> environment.setProperty(prefix + "system.port", String.valueOf(port)));

    config
        .device()
        .wdaLocalPort()
        .ifPresent(
            port -> environment.setProperty(prefix + "wda.local.port", String.valueOf(port)));
  }

  private static void save(Properties environment, Path resultsDirectory) throws IOException {

    Files.createDirectories(resultsDirectory);

    Path environmentFile = resultsDirectory.resolve(ENVIRONMENT_FILE);

    try (Writer writer = Files.newBufferedWriter(environmentFile, UTF_8)) {

      environment.store(writer, "Generated automatically for the Allure report");
    }

    LOGGER.info("Saved Allure environment information: {}", environmentFile.toAbsolutePath());
  }

  private static String safeServerAddress(URI appiumUrl) {
    StringBuilder address =
        new StringBuilder().append(appiumUrl.getScheme()).append("://").append(appiumUrl.getHost());

    if (appiumUrl.getPort() >= 0) {
      address.append(":").append(appiumUrl.getPort());
    }

    String path = appiumUrl.getPath();

    if (path != null && !path.isBlank() && !path.equals("/")) {

      address.append(path);
    }

    return address.toString();
  }
}
