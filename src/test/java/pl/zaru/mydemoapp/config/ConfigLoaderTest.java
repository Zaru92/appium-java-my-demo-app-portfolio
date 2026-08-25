package pl.zaru.mydemoapp.config;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import org.testng.annotations.Test;

public final class ConfigLoaderTest {
  @Test
  public void shouldLoadAndroidConfigurationByDefault() {
    TestConfig config = ConfigLoader.load(Map.of());

    assertEquals(config.platform(), Platform.ANDROID);
    assertEquals(config.automationName(), "UiAutomator2");
    assertEquals(config.device().targetType(), TargetType.EMULATOR);
    assertEquals(config.device().deviceName(), "Pixel_8");
    assertEquals(config.device().udid().orElseThrow(), "emulator-5554");
    assertTrue(config.device().platformVersion().isEmpty());
    assertTrue(
        config
            .appPath()
            .endsWith(Path.of("src/test/resources/apps/" + "my-demo-app-android-2.2.0.apk")));
    assertEquals(config.newCommandTimeout(), Duration.ofSeconds(120));
    assertEquals(config.device().systemPort(), Optional.of(8200));
    assertTrue(config.device().wdaLocalPort().isEmpty());
  }

  @Test
  public void shouldLoadIosConfiguration() {
    TestConfig config = ConfigLoader.load(Map.of("platform", "ios"));

    assertEquals(config.platform(), Platform.IOS);
    assertEquals(config.automationName(), "XCUITest");
    assertEquals(config.device().targetType(), TargetType.SIMULATOR);
    assertEquals(config.device().deviceName(), "iPhone 17 Pro");
    assertEquals(config.device().platformVersion().orElseThrow(), "26.4");
    assertTrue(config.device().udid().isEmpty());
    assertTrue(
        config
            .appPath()
            .endsWith(Path.of("src/test/resources/apps/" + "my-demo-app-ios-simulator-2.2.2.zip")));
    assertTrue(config.device().systemPort().isEmpty());
    assertEquals(config.device().wdaLocalPort(), Optional.of(8100));
  }

  @Test
  public void shouldApplyCommandLineStyleOverrides() {
    TestConfig config =
        ConfigLoader.load(
            Map.of(
                "appium.url", "http://127.0.0.1:4725",
                "deviceName", "Pixel 7",
                "udid", "physical-device-udid",
                "newCommandTimeoutSeconds", "180",
                "targetType", "real"));

    assertEquals(config.appiumUrl(), URI.create("http://127.0.0.1:4725"));
    assertEquals(config.device().deviceName(), "Pixel 7");
    assertEquals(config.device().udid().orElseThrow(), "physical-device-udid");
    assertEquals(config.newCommandTimeout(), Duration.ofSeconds(180));
    assertEquals(config.device().targetType(), TargetType.REAL_DEVICE);
  }

  @Test(
      expectedExceptions = IllegalStateException.class,
      expectedExceptionsMessageRegExp = "newCommandTimeoutSeconds must be positive\\.")
  public void shouldRejectNonPositiveTimeout() {
    ConfigLoader.load(Map.of("newCommandTimeoutSeconds", "0"));
  }

  @Test(
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Target type emulator is not supported for platform ios\\.")
  public void shouldRejectIncompatibleTargetType() {
    ConfigLoader.load(Map.of("platform", "ios", "targetType", "emulator"));
  }
}
