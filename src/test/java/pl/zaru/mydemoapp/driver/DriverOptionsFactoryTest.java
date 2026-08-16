package pl.zaru.mydemoapp.driver;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import org.openqa.selenium.Capabilities;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.config.MobilePlatform;
import pl.zaru.mydemoapp.config.TestConfig;

public final class DriverOptionsFactoryTest {

  @Test
  public void shouldBuildAndroidOptions() {
    TestConfig config = androidConfig();

    Capabilities options = DriverOptionsFactory.create(config);

    assertTrue(options instanceof UiAutomator2Options);
    assertAutomationName(options, config);
    assertEquals(options.getCapability("appium:deviceName"), "Pixel_9");
    assertEquals(options.getCapability("appium:udid"), "emulator-5554");
    assertEquals(options.getCapability("appium:app"), config.appPath().toString());
    assertEquals(timeoutInSeconds(options), 120L);
    assertEquals(
        options.getCapability("appium:appWaitActivity"),
        "com.saucelabs.mydemoapp.android.view.activities.MainActivity");
  }

  @Test
  public void shouldBuildIosOptions() {
    TestConfig config = iosConfig();

    Capabilities options = DriverOptionsFactory.create(config);

    assertTrue(options instanceof XCUITestOptions);
    assertAutomationName(options, config);
    assertEquals(options.getCapability("appium:deviceName"), "iPhone 17 Pro");
    assertEquals(options.getCapability("appium:platformVersion"), "26.4");
    assertEquals(options.getCapability("appium:app"), config.appPath().toString());
    assertEquals(timeoutInSeconds(options), 120L);
  }

  private static void assertAutomationName(Capabilities options, TestConfig config) {
    String automationName = String.valueOf(options.getCapability("appium:automationName"));

    assertTrue(automationName.equalsIgnoreCase(config.automationName()));
  }

  private static long timeoutInSeconds(Capabilities options) {
    Number timeout = (Number) options.getCapability("appium:newCommandTimeout");

    return timeout.longValue();
  }

  private static TestConfig androidConfig() {
    return new TestConfig(
        URI.create("http://127.0.0.1:4723"),
        MobilePlatform.ANDROID,
        "Pixel_9",
        Optional.of("emulator-5554"),
        Optional.empty(),
        Optional.of("com.saucelabs.mydemoapp.android.view.activities.MainActivity"),
        Path.of("src/test/resources/apps/" + "my-demo-app-android-2.2.0.apk"),
        Duration.ofSeconds(120));
  }

  private static TestConfig iosConfig() {
    return new TestConfig(
        URI.create("http://127.0.0.1:4723"),
        MobilePlatform.IOS,
        "iPhone 17 Pro",
        Optional.empty(),
        Optional.of("26.4"),
        Optional.empty(),
        Path.of("src/test/resources/apps/" + "my-demo-app-ios-simulator-2.2.2.zip"),
        Duration.ofSeconds(120));
  }
}
