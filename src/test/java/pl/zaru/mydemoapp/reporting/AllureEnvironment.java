package pl.zaru.mydemoapp.reporting;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
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

  static void write(TestConfig config, Path resultsDirectory) throws IOException {

    Objects.requireNonNull(config, "config must not be null");
    Objects.requireNonNull(resultsDirectory, "resultsDirectory must not be null");

    Files.createDirectories(resultsDirectory);

    Properties environment = new Properties();

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

    environment.setProperty("java.version", System.getProperty("java.version", "unknown"));

    environment.setProperty("os.name", System.getProperty("os.name", "unknown"));

    environment.setProperty("os.version", System.getProperty("os.version", "unknown"));

    environment.setProperty("os.architecture", System.getProperty("os.arch", "unknown"));

    config
        .device()
        .systemPort()
        .ifPresent(port -> environment.setProperty("system.port", String.valueOf(port)));

    config
        .device()
        .wdaLocalPort()
        .ifPresent(port -> environment.setProperty("wda.local.port", String.valueOf(port)));

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
