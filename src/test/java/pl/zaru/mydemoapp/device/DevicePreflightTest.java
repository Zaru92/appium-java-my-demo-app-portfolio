package pl.zaru.mydemoapp.device;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.Platform;
import pl.zaru.mydemoapp.config.TargetType;
import pl.zaru.mydemoapp.config.TestConfig;

public final class DevicePreflightTest {

  @Test
  public void shouldUseAdbCheckerForAndroid() {
    AtomicBoolean adbCalled = new AtomicBoolean();
    AtomicBoolean simctlCalled = new AtomicBoolean();
    AtomicBoolean xcuitestCalled = new AtomicBoolean();

    DevicePreflight preflight =
        new DevicePreflight(
            config -> {},
            config -> {},
            device -> adbCalled.set(true),
            device -> simctlCalled.set(true),
            device -> xcuitestCalled.set(true));

    preflight.verify(androidConfig());

    assertTrue(adbCalled.get());
    assertFalse(simctlCalled.get());
    assertFalse(xcuitestCalled.get());
  }

  @Test
  public void shouldUseSimctlCheckerForIosSimulator() {
    AtomicBoolean adbCalled = new AtomicBoolean();
    AtomicBoolean simctlCalled = new AtomicBoolean();
    AtomicBoolean xcuitestCalled = new AtomicBoolean();

    DevicePreflight preflight =
        new DevicePreflight(
            config -> {},
            config -> {},
            device -> adbCalled.set(true),
            device -> simctlCalled.set(true),
            device -> xcuitestCalled.set(true));

    preflight.verify(iosSimulatorConfig());

    assertFalse(adbCalled.get());
    assertTrue(simctlCalled.get());
    assertFalse(xcuitestCalled.get());
  }

  @Test
  public void shouldUseXcuitestCheckerForRealIosDevice() {
    AtomicBoolean adbCalled = new AtomicBoolean();
    AtomicBoolean simctlCalled = new AtomicBoolean();
    AtomicBoolean xcuitestCalled = new AtomicBoolean();

    DevicePreflight preflight =
        new DevicePreflight(
            config -> {},
            config -> {},
            device -> adbCalled.set(true),
            device -> simctlCalled.set(true),
            device -> xcuitestCalled.set(true));

    preflight.verify(realIosDeviceConfig());

    assertFalse(adbCalled.get());
    assertFalse(simctlCalled.get());
    assertTrue(xcuitestCalled.get());
  }

  private static TestConfig androidConfig() {
    return config(
        Platform.ANDROID,
        new DeviceConfig(
            TargetType.EMULATOR, "Pixel_8", Optional.of("emulator-5554"), Optional.empty()));
  }

  private static TestConfig iosSimulatorConfig() {
    return config(
        Platform.IOS,
        new DeviceConfig(
            TargetType.SIMULATOR, "iPhone 17 Pro", Optional.empty(), Optional.of("26.4")));
  }

  private static TestConfig realIosDeviceConfig() {
    return config(
        Platform.IOS,
        new DeviceConfig(
            TargetType.REAL_DEVICE,
            "iPhone",
            Optional.of("real-device-udid"),
            Optional.of("26.4")));
  }

  private static TestConfig config(Platform platform, DeviceConfig device) {
    return new TestConfig(
        URI.create("http://127.0.0.1:4723"),
        platform,
        device,
        Optional.empty(),
        Path.of("app-under-test"),
        Duration.ofSeconds(120));
  }
}
