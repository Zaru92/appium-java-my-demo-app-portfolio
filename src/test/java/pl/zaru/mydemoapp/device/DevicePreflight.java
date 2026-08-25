package pl.zaru.mydemoapp.device;

import java.util.Objects;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.Platform;
import pl.zaru.mydemoapp.config.TargetType;
import pl.zaru.mydemoapp.config.TestConfig;

public final class DevicePreflight {
  private final DeviceChecker adbDeviceChecker;
  private final DeviceChecker simctlDeviceChecker;

  public DevicePreflight() {
    this(new AdbDeviceChecker(), new SimctlDeviceChecker());
  }

  DevicePreflight(DeviceChecker adbDeviceChecker, DeviceChecker simctlDeviceChecker) {

    this.adbDeviceChecker =
        Objects.requireNonNull(adbDeviceChecker, "adbDeviceChecker must not be null");

    this.simctlDeviceChecker =
        Objects.requireNonNull(simctlDeviceChecker, "simctlDeviceChecker must not be null");
  }

  public void verify(TestConfig config) {
    Objects.requireNonNull(config, "config must not be null");

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

    throw new UnsupportedOperationException(
        "Preflight for a real iOS device is not supported yet.");
  }
}
