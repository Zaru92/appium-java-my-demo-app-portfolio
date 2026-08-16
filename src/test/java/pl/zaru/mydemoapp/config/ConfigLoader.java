package pl.zaru.mydemoapp.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

public final class ConfigLoader {
  private static final String CONFIG_DIRECTORY = "config/";

  private static final Set<String> OVERRIDABLE_KEYS =
      Set.of(
          "platform",
          "appium.url",
          "deviceName",
          "udid",
          "platformVersion",
          "app",
          "newCommandTimeoutSeconds",
          "appWaitActivity");

  private ConfigLoader() {}

  public static TestConfig load() {
    return load(systemOverrides());
  }

  static TestConfig load(Map<String, String> overrides) {
    Objects.requireNonNull(overrides, "overrides must not be null");

    Properties properties = loadProperties("common.properties");

    String selectedPlatform = overrides.getOrDefault("platform", required(properties, "platform"));

    MobilePlatform platform = MobilePlatform.from(selectedPlatform);

    properties.putAll(loadProperties(platform.configFile()));
    applyOverrides(properties, overrides);

    return new TestConfig(
        URI.create(required(properties, "appium.url")),
        MobilePlatform.from(required(properties, "platform")),
        required(properties, "deviceName"),
        optional(properties, "udid"),
        optional(properties, "platformVersion"),
        optional(properties, "appWaitActivity"),
        Path.of(required(properties, "app")),
        Duration.ofSeconds(positiveLong(properties, "newCommandTimeoutSeconds")));
  }

  private static Map<String, String> systemOverrides() {
    Map<String, String> overrides = new HashMap<>();

    for (String key : OVERRIDABLE_KEYS) {
      String value = System.getProperty(key);

      if (value != null) {
        overrides.put(key, value);
      }
    }

    return overrides;
  }

  private static Properties loadProperties(String fileName) {
    String resourceName = CONFIG_DIRECTORY + fileName;

    try (InputStream input =
        ConfigLoader.class.getClassLoader().getResourceAsStream(resourceName)) {

      if (input == null) {
        throw new IllegalStateException("Missing configuration resource: " + resourceName);
      }

      Properties properties = new Properties();
      properties.load(input);
      return properties;
    } catch (IOException exception) {
      throw new UncheckedIOException("Could not load configuration: " + resourceName, exception);
    }
  }

  private static void applyOverrides(Properties properties, Map<String, String> overrides) {
    overrides.forEach(
        (key, value) -> {
          if (OVERRIDABLE_KEYS.contains(key)) {
            properties.setProperty(key, value);
          }
        });
  }

  private static String required(Properties properties, String key) {
    String value = properties.getProperty(key);

    if (value == null || value.isBlank()) {
      throw new IllegalStateException("Missing required configuration property: " + key);
    }

    return value.trim();
  }

  private static Optional<String> optional(Properties properties, String key) {
    return Optional.ofNullable(properties.getProperty(key))
        .map(String::trim)
        .filter(value -> !value.isEmpty());
  }

  private static long positiveLong(Properties properties, String key) {
    String value = required(properties, key);

    try {
      long parsedValue = Long.parseLong(value);

      if (parsedValue <= 0) {
        throw new IllegalStateException(key + " must be positive.");
      }

      return parsedValue;
    } catch (NumberFormatException exception) {
      throw new IllegalStateException(key + " must be a whole number: " + value, exception);
    }
  }
}
