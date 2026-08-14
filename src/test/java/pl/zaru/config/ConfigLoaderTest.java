package pl.zaru.config;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import org.testng.annotations.Test;

public final class ConfigLoaderTest {
  @Test
  public void shouldLoadAndroidConfigurationByDefault() {
    TestConfig config = ConfigLoader.load(Map.of());

    assertEquals(config.platform(), MobilePlatform.ANDROID);
    assertEquals(config.automationName(), "UiAutomator2");
    assertEquals(config.deviceName(), "Pixel_8");
    assertEquals(config.udid().orElseThrow(), "emulator-5556");
    assertTrue(config.platformVersion().isEmpty());
    assertTrue(
        config
            .appPath()
            .endsWith(Path.of("src/test/resources/apps/" + "my-demo-app-android-2.2.0.apk")));
    assertEquals(config.newCommandTimeout(), Duration.ofSeconds(120));
  }

  @Test
  public void shouldLoadIosConfiguration() {
    TestConfig config = ConfigLoader.load(Map.of("platform", "ios"));

    assertEquals(config.platform(), MobilePlatform.IOS);
    assertEquals(config.automationName(), "XCUITest");
    assertEquals(config.deviceName(), "iPhone 17 Pro");
    assertEquals(config.platformVersion().orElseThrow(), "26.4");
    assertTrue(config.udid().isEmpty());
    assertTrue(
        config
            .appPath()
            .endsWith(Path.of("src/test/resources/apps/" + "my-demo-app-ios-simulator-2.2.2.zip")));
  }

  @Test
  public void shouldApplyCommandLineStyleOverrides() {
    TestConfig config =
        ConfigLoader.load(
            Map.of(
                "appium.url", "http://127.0.0.1:4725",
                "deviceName", "Pixel 7",
                "udid", "physical-device-udid",
                "newCommandTimeoutSeconds", "180"));

    assertEquals(config.appiumUrl(), URI.create("http://127.0.0.1:4725"));
    assertEquals(config.deviceName(), "Pixel 7");
    assertEquals(config.udid().orElseThrow(), "physical-device-udid");
    assertEquals(config.newCommandTimeout(), Duration.ofSeconds(180));
  }

  @Test(
      expectedExceptions = IllegalStateException.class,
      expectedExceptionsMessageRegExp = "newCommandTimeoutSeconds must be positive\\.")
  public void shouldRejectNonPositiveTimeout() {
    ConfigLoader.load(Map.of("newCommandTimeoutSeconds", "0"));
  }
}
