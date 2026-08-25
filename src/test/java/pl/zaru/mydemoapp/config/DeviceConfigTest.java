package pl.zaru.mydemoapp.config;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Optional;
import org.testng.annotations.Test;

public final class DeviceConfigTest {

  @Test
  public void shouldCreateDeviceConfigWithSystemPort() {
    DeviceConfig config =
        new DeviceConfig(
            TargetType.EMULATOR,
            "Pixel 8",
            Optional.of("emulator-5554"),
            Optional.empty(),
            Optional.of(8200),
            Optional.empty());

    assertEquals(config.systemPort(), Optional.of(8200));
    assertTrue(config.wdaLocalPort().isEmpty());
  }

  @Test(
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "systemPort must be between 1 and 65535\\.")
  public void shouldRejectInvalidSystemPort() {
    new DeviceConfig(
        TargetType.EMULATOR,
        "Pixel 8",
        Optional.of("emulator-5554"),
        Optional.empty(),
        Optional.of(0),
        Optional.empty());
  }

  @Test(
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "wdaLocalPort must be between 1 and 65535\\.")
  public void shouldRejectInvalidWdaLocalPort() {
    new DeviceConfig(
        TargetType.SIMULATOR,
        "iPhone 17 Pro",
        Optional.empty(),
        Optional.of("26.4"),
        Optional.empty(),
        Optional.of(65536));
  }

  @Test(
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp =
          "Only one platform-specific automation port may be configured\\.")
  public void shouldRejectTwoAutomationPorts() {
    new DeviceConfig(
        TargetType.EMULATOR,
        "Pixel 8",
        Optional.of("emulator-5554"),
        Optional.empty(),
        Optional.of(8200),
        Optional.of(8100));
  }
}
