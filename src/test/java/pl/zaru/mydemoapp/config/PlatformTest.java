package pl.zaru.mydemoapp.config;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public final class PlatformTest {
  @DataProvider(name = "supportedPlatforms")
  public Object[][] supportedPlatforms() {
    return new Object[][] {
      {"android", Platform.ANDROID},
      {"ANDROID", Platform.ANDROID},
      {"ios", Platform.IOS},
      {"iOS", Platform.IOS}
    };
  }

  @Test(dataProvider = "supportedPlatforms")
  public void shouldParsePlatformIgnoringCase(String value, Platform expectedPlatform) {
    assertEquals(Platform.from(value), expectedPlatform);
  }

  @Test
  public void shouldExposeDriverMetadata() {
    assertEquals(Platform.ANDROID.automationName(), "UiAutomator2");
    assertEquals(Platform.ANDROID.configFile(), "android.properties");

    assertEquals(Platform.IOS.automationName(), "XCUITest");
    assertEquals(Platform.IOS.configFile(), "ios.properties");
  }

  @Test(
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp =
          "Unsupported platform: windows\\. Expected android or ios\\.")
  public void shouldRejectUnsupportedPlatform() {
    Platform.from("windows");
  }
}
