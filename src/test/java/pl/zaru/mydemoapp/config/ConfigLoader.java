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
          "targetType",
          "appium.url",
          "deviceName",
          "udid",
          "platformVersion",
          "app",
          "newCommandTimeoutSeconds",
          "appWaitActivity",
          "systemPort",
          "wdaLocalPort");

  private ConfigLoader() {}

  public static TestConfig load() {
    return createConfig(systemOverrides());
  }

  public static TestConfig load(Map<String, String> overrides) {
    Objects.requireNonNull(overrides, "overrides must not be null");

    Map<String, String> mergedOverrides = new HashMap<>(overrides);
    mergedOverrides.putAll(systemOverrides());

    return createConfig(mergedOverrides);
  }

  private static TestConfig createConfig(Map<String, String> overrides) {
    Properties properties = loadProperties("common.properties");

    Platform selectedPlatform =
        Platform.from(overrides.getOrDefault("platform", required(properties, "platform")));

    properties.putAll(loadProperties(selectedPlatform.configFile()));

    applyOverrides(properties, overrides);

    Platform platform = Platform.from(required(properties, "platform"));

    DeviceConfig device =
        new DeviceConfig(
            TargetType.from(required(properties, "targetType")),
            required(properties, "deviceName"),
            optional(properties, "udid"),
            optional(properties, "platformVersion"),
            optionalPort(properties, "systemPort"),
            optionalPort(properties, "wdaLocalPort"));

    return new TestConfig(
        URI.create(required(properties, "appium.url")),
        platform,
        device,
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

  private static Optional<Integer> optionalPort(Properties properties, String key) {

    return optional(properties, key)
        .map(
            value -> {
              try {
                int port = Integer.parseInt(value);

                if (port < 1 || port > 65535) {
                  throw new IllegalStateException(key + " must be between 1 and 65535.");
                }

                return port;
              } catch (NumberFormatException exception) {
                throw new IllegalStateException(
                    key + " must be a whole number: " + value, exception);
              }
            });
  }
}
