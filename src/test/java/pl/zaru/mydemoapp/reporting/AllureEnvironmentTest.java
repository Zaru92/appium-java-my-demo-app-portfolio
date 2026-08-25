package pl.zaru.mydemoapp.reporting;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.Platform;
import pl.zaru.mydemoapp.config.TargetType;
import pl.zaru.mydemoapp.config.TestConfig;

public final class AllureEnvironmentTest {

  @Test
  public void shouldWriteEnvironmentProperties() throws IOException {
    Path resultsDirectory = Files.createTempDirectory("allure-results-");

    Path environmentFile = resultsDirectory.resolve("environment.properties");

    try {
      AllureEnvironment.write(config(), resultsDirectory);

      assertTrue(Files.isRegularFile(environmentFile));

      Properties properties = new Properties();

      try (Reader reader = Files.newBufferedReader(environmentFile, UTF_8)) {

        properties.load(reader);
      }

      assertEquals(properties.getProperty("platform"), "android");
      assertEquals(properties.getProperty("target.type"), "emulator");
      assertEquals(properties.getProperty("device.name"), "Pixel_8");
      assertEquals(properties.getProperty("automation.name"), "UiAutomator2");
      assertEquals(properties.getProperty("application"), "my-demo-app.apk");
      assertEquals(properties.getProperty("appium.server"), "http://127.0.0.1:4723");
    } finally {
      Files.deleteIfExists(environmentFile);
      Files.deleteIfExists(resultsDirectory);
    }
  }

  private static TestConfig config() {
    return new TestConfig(
        URI.create("http://127.0.0.1:4723"),
        Platform.ANDROID,
        new DeviceConfig(
            TargetType.EMULATOR, "Pixel_8", Optional.of("emulator-5554"), Optional.empty()),
        Optional.empty(),
        Path.of("my-demo-app.apk"),
        Duration.ofSeconds(120));
  }
}
