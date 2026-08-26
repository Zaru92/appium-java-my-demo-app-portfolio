package pl.zaru.mydemoapp.device;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.Platform;
import pl.zaru.mydemoapp.config.TargetType;
import pl.zaru.mydemoapp.config.TestConfig;

public class ParallelConfigValidatorTest {

  private final ParallelConfigValidator validator = new ParallelConfigValidator();

  @Test
  public void shouldAcceptUniqueParallelConfigurations() {
    Map<String, TestConfig> configs = new LinkedHashMap<>();

    configs.put("Android", androidConfig("android-1", 8200));

    configs.put("iOS", iosConfig("ios-1", 8100));

    validator.verify(configs);
  }

  @Test
  public void shouldRejectDuplicateAutomationPort() {
    Map<String, TestConfig> configs = new LinkedHashMap<>();

    configs.put("Android", androidConfig("android-1", 8100));

    configs.put("iOS", iosConfig("ios-1", 8100));

    IllegalStateException exception =
        expectThrows(IllegalStateException.class, () -> validator.verify(configs));

    assertTrue(exception.getMessage().contains("Automation port 8100"));
  }

  @Test
  public void shouldRejectMissingUdid() {
    Map<String, TestConfig> configs = new LinkedHashMap<>();

    configs.put("Android", androidConfig("android-1", 8200));

    configs.put("iOS", iosConfig(null, 8100));

    IllegalStateException exception =
        expectThrows(IllegalStateException.class, () -> validator.verify(configs));

    assertTrue(exception.getMessage().contains("must define udid"));
  }

  private static TestConfig androidConfig(String udid, int systemPort) {

    return new TestConfig(
        URI.create("http://127.0.0.1:4723"),
        Platform.ANDROID,
        new DeviceConfig(
            TargetType.EMULATOR,
            "Pixel 8",
            Optional.ofNullable(udid),
            Optional.empty(),
            Optional.of(systemPort),
            Optional.empty()),
        Optional.empty(),
        Path.of("app.apk"),
        Duration.ofSeconds(120));
  }

  private static TestConfig iosConfig(String udid, int wdaLocalPort) {

    return new TestConfig(
        URI.create("http://127.0.0.1:4723"),
        Platform.IOS,
        new DeviceConfig(
            TargetType.SIMULATOR,
            "iPhone 17 Pro",
            Optional.ofNullable(udid),
            Optional.of("26.4"),
            Optional.empty(),
            Optional.of(wdaLocalPort)),
        Optional.empty(),
        Path.of("app.zip"),
        Duration.ofSeconds(120));
  }
}
