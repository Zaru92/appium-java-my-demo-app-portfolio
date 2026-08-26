package pl.zaru.mydemoapp.device;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import pl.zaru.mydemoapp.config.TestConfig;

public final class ParallelConfigValidator {

  public void verify(Map<String, TestConfig> configs) {
    Objects.requireNonNull(configs, "configs must not be null");

    if (configs.size() < 2) {
      return;
    }

    Map<String, String> udidOwners = new HashMap<>();
    Map<Integer, PortOwner> portOwners = new HashMap<>();

    configs.forEach(
        (rawTestName, config) -> {
          String testName = requireTestName(rawTestName);

          Objects.requireNonNull(
              config, "config for test '%s' must not be null".formatted(testName));

          String udid =
              config
                  .device()
                  .udid()
                  .orElseThrow(
                      () ->
                          new IllegalStateException(
                              "Parallel test '%s' must define udid.".formatted(testName)));

          registerUdid(udidOwners, udid, testName);

          switch (config.platform()) {
            case ANDROID -> {
              int systemPort =
                  config
                      .device()
                      .systemPort()
                      .orElseThrow(
                          () ->
                              new IllegalStateException(
                                  "Parallel Android test '%s' must define systemPort."
                                      .formatted(testName)));

              registerPort(portOwners, systemPort, testName, "systemPort");
            }

            case IOS -> {
              int wdaLocalPort =
                  config
                      .device()
                      .wdaLocalPort()
                      .orElseThrow(
                          () ->
                              new IllegalStateException(
                                  "Parallel iOS test '%s' must define wdaLocalPort."
                                      .formatted(testName)));

              registerPort(portOwners, wdaLocalPort, testName, "wdaLocalPort");
            }
          }
        });
  }

  private static String requireTestName(String testName) {
    if (testName == null || testName.isBlank()) {
      throw new IllegalArgumentException("TestNG test name must not be blank.");
    }

    return testName;
  }

  private static void registerUdid(Map<String, String> owners, String udid, String testName) {

    String existingOwner = owners.putIfAbsent(udid, testName);

    if (existingOwner != null) {
      throw new IllegalStateException(
          "udid '%s' is configured for both '%s' and '%s'."
              .formatted(udid, existingOwner, testName));
    }
  }

  private static void registerPort(
      Map<Integer, PortOwner> owners, int port, String testName, String capabilityName) {

    PortOwner newOwner = new PortOwner(testName, capabilityName);

    PortOwner existingOwner = owners.putIfAbsent(port, newOwner);

    if (existingOwner != null) {
      throw new IllegalStateException(
          "Automation port %d is configured for both '%s' (%s) and '%s' (%s)."
              .formatted(
                  port,
                  existingOwner.testName(),
                  existingOwner.capabilityName(),
                  testName,
                  capabilityName));
    }
  }

  private record PortOwner(String testName, String capabilityName) {}
}
