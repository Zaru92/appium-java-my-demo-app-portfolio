package pl.zaru.config;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public final class MobilePlatformTest {
  @DataProvider(name = "supportedPlatforms")
  public Object[][] supportedPlatforms() {
    return new Object[][] {
      {"android", MobilePlatform.ANDROID},
      {"ANDROID", MobilePlatform.ANDROID},
      {"ios", MobilePlatform.IOS},
      {"iOS", MobilePlatform.IOS}
    };
  }

  @Test(dataProvider = "supportedPlatforms")
  public void shouldParsePlatformIgnoringCase(String value, MobilePlatform expectedPlatform) {
    assertEquals(MobilePlatform.from(value), expectedPlatform);
  }

  @Test
  public void shouldExposeDriverMetadata() {
    assertEquals(MobilePlatform.ANDROID.automationName(), "UiAutomator2");
    assertEquals(MobilePlatform.ANDROID.configFile(), "android.properties");

    assertEquals(MobilePlatform.IOS.automationName(), "XCUITest");
    assertEquals(MobilePlatform.IOS.configFile(), "ios.properties");
  }

  @Test(
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp =
          "Unsupported platform: windows\\. Expected android or ios\\.")
  public void shouldRejectUnsupportedPlatform() {
    MobilePlatform.from("windows");
  }
}
