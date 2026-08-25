package pl.zaru.mydemoapp.device;

import pl.zaru.mydemoapp.config.DeviceConfig;

@FunctionalInterface
public interface DeviceChecker {
  void verify(DeviceConfig device);
}
