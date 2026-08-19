package pl.zaru.mydemoapp.driver;

import java.util.Objects;
import org.openqa.selenium.Capabilities;
import pl.zaru.mydemoapp.config.TestConfig;

final class DriverOptionsFactory {

  private DriverOptionsFactory() {}

  static Capabilities create(TestConfig config) {
    Objects.requireNonNull(config, "config must not be null");

    return switch (config.platform()) {
      case ANDROID -> AndroidOptionsFactory.create(config);
      case IOS -> IosOptionsFactory.create(config);
    };
  }
}
