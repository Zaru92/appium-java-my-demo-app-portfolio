package pl.zaru.mydemoapp.pages;

import io.appium.java_client.AppiumDriver;
import java.time.Duration;
import java.util.Objects;
import pl.zaru.mydemoapp.config.TargetType;

public record PageContext(AppiumDriver driver, TargetType targetType, Duration waitTimeout) {

  public PageContext {
    Objects.requireNonNull(driver, "driver must not be null");
    Objects.requireNonNull(targetType, "targetType must not be null");
    Objects.requireNonNull(waitTimeout, "waitTimeout must not be null");

    if (waitTimeout.isZero() || waitTimeout.isNegative()) {
      throw new IllegalArgumentException("waitTimeout must be positive");
    }
  }
}
