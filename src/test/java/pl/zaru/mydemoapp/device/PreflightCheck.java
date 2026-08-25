package pl.zaru.mydemoapp.device;

import pl.zaru.mydemoapp.config.TestConfig;

@FunctionalInterface
public interface PreflightCheck {
  void verify(TestConfig config);
}
