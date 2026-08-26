package pl.zaru.mydemoapp.device;

import java.util.Objects;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.Platform;
import pl.zaru.mydemoapp.config.TargetType;
import pl.zaru.mydemoapp.config.TestConfig;

public final class DevicePreflight {
  private final PreflightCheck appBinaryChecker;
  private final PreflightCheck appiumServerChecker;
  private final DeviceChecker adbDeviceChecker;
  private final DeviceChecker simctlDeviceChecker;
  private final DeviceChecker xcuitestDeviceChecker;

  PortChecker portChecker = new PortChecker();

  public DevicePreflight() {
    this(
        new AppBinaryChecker(),
        new AppiumServerChecker(),
        new AdbDeviceChecker(),
        new SimctlDeviceChecker(),
        new XcuitestDeviceChecker());
  }

  DevicePreflight(
      PreflightCheck appBinaryChecker,
      PreflightCheck appiumServerChecker,
      DeviceChecker adbDeviceChecker,
      DeviceChecker simctlDeviceChecker,
      DeviceChecker xcuitestDeviceChecker) {

    this.appBinaryChecker =
        Objects.requireNonNull(appBinaryChecker, "appBinaryChecker must not be null");

    this.appiumServerChecker =
        Objects.requireNonNull(appiumServerChecker, "appiumServerChecker must not be null");

    this.adbDeviceChecker =
        Objects.requireNonNull(adbDeviceChecker, "adbDeviceChecker must not be null");

    this.simctlDeviceChecker =
        Objects.requireNonNull(simctlDeviceChecker, "simctlDeviceChecker must not be null");

    this.xcuitestDeviceChecker =
        Objects.requireNonNull(xcuitestDeviceChecker, "xcuitestDeviceChecker must not be null");
  }

  public void verify(TestConfig config) {
    Objects.requireNonNull(config, "config must not be null");

    appBinaryChecker.verify(config);
    appiumServerChecker.verify(config);

    verifySystemPort(config);

    if (config.platform() == Platform.ANDROID) {
      adbDeviceChecker.verify(config.device());
      return;
    }

    verifyIos(config.device());
  }

  private void verifyIos(DeviceConfig device) {
    if (device.targetType() == TargetType.SIMULATOR) {
      simctlDeviceChecker.verify(device);
      return;
    }

    xcuitestDeviceChecker.verify(device);
  }

  private void verifySystemPort(TestConfig config) {
    config.device().systemPort().ifPresent(port -> portChecker.verifyAvailable("systemPort", port));
  }
}
