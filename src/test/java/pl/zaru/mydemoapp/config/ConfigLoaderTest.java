package pl.zaru.mydemoapp.config;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public final class ConfigLoaderTest {

  private static final String PLATFORM_KEY = "platform";
  private static final String TARGET_TYPE_KEY = "targetType";

  private static final Set<String> CONFIG_SYSTEM_PROPERTIES =
      Set.of(
          "appium.url",
          PLATFORM_KEY,
          "automationName",
          TARGET_TYPE_KEY,
          "deviceName",
          "platformVersion",
          "udid",
          "app",
          "appPath",
          "newCommandTimeoutSeconds",
          "waitTimeoutSeconds",
          "systemPort",
          "wdaLocalPort");

  private final Map<String, String> originalSystemProperties = new HashMap<>();

  @BeforeMethod(alwaysRun = true)
  public void clearConfigSystemProperties() {
    originalSystemProperties.clear();

    for (String property : CONFIG_SYSTEM_PROPERTIES) {
      String originalValue = System.getProperty(property);

      if (originalValue != null) {
        originalSystemProperties.put(property, originalValue);
      }

      System.clearProperty(property);
    }
  }

  @AfterMethod(alwaysRun = true)
  public void restoreConfigSystemProperties() {
    for (String property : CONFIG_SYSTEM_PROPERTIES) {
      System.clearProperty(property);
    }

    originalSystemProperties.forEach(System::setProperty);
  }

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
  public void shouldLoadDefaultWaitTimeout() {
    TestConfig config = ConfigLoader.load(Map.of());

    assertEquals(config.waitTimeout(), Duration.ofSeconds(10));
  }

  @Test
  public void shouldLoadIosConfiguration() {
    TestConfig config = ConfigLoader.load(Map.of(PLATFORM_KEY, "ios"));

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
                "appium.url",
                "http://127.0.0.1:4725",
                "deviceName",
                "Pixel 7",
                "udid",
                "physical-device-udid",
                "newCommandTimeoutSeconds",
                "180",
                "waitTimeoutSeconds",
                "25",
                TARGET_TYPE_KEY,
                "real"));

    assertEquals(config.appiumUrl(), URI.create("http://127.0.0.1:4725"));
    assertEquals(config.device().deviceName(), "Pixel 7");
    assertEquals(config.device().udid().orElseThrow(), "physical-device-udid");
    assertEquals(config.newCommandTimeout(), Duration.ofSeconds(180));
    assertEquals(config.waitTimeout(), Duration.ofSeconds(25));
    assertEquals(config.device().targetType(), TargetType.REAL_DEVICE);
  }

  @Test(
      expectedExceptions = IllegalStateException.class,
      expectedExceptionsMessageRegExp = "newCommandTimeoutSeconds must be positive\\.")
  public void shouldRejectNonPositiveTimeout() {
    ConfigLoader.load(Map.of("newCommandTimeoutSeconds", "0"));
  }

  @Test(
      expectedExceptions = IllegalStateException.class,
      expectedExceptionsMessageRegExp = "waitTimeoutSeconds must be positive\\.")
  public void shouldRejectNonPositiveWaitTimeout() {
    ConfigLoader.load(Map.of("waitTimeoutSeconds", "0"));
  }

  @Test(
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Target type emulator is not supported for platform ios\\.")
  public void shouldRejectIncompatibleTargetType() {
    ConfigLoader.load(Map.of(PLATFORM_KEY, "ios", TARGET_TYPE_KEY, "emulator"));
  }

  @Test
  public void shouldLoadConfigurationFromRuntimeOverrides() {
    TestConfig config =
        ConfigLoader.load(
            Map.of(
                PLATFORM_KEY,
                "android",
                TARGET_TYPE_KEY,
                "emulator",
                "deviceName",
                "Parallel Pixel 8",
                "udid",
                "emulator-5554",
                "systemPort",
                "8201"));

    assertEquals(config.platform(), Platform.ANDROID);
    assertEquals(config.device().deviceName(), "Parallel Pixel 8");
    assertEquals(config.device().udid(), Optional.of("emulator-5554"));
    assertEquals(config.device().systemPort(), Optional.of(8201));
    assertTrue(config.device().wdaLocalPort().isEmpty());
  }
}
