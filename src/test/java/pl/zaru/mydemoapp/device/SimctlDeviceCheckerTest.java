package pl.zaru.mydemoapp.device;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

import java.util.Optional;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.TargetType;

public final class SimctlDeviceCheckerTest {

  @Test
  public void shouldAcceptBootedIosSimulator() {
    CommandExecutor executor =
        (command, timeout) ->
            new CommandResult(
                0,
                """
                    == Devices ==
                    -- iOS 26.4 --
                        iPhone 17 Pro (ABC-123) (Booted)
                    """);

    new SimctlDeviceChecker(executor).verify(iosSimulator());
  }

  @Test
  public void shouldRejectUnavailableIosSimulator() {
    CommandExecutor executor = (command, timeout) -> new CommandResult(0, "== Devices ==");

    IllegalStateException exception =
        expectThrows(
            IllegalStateException.class,
            () -> new SimctlDeviceChecker(executor).verify(iosSimulator()));

    assertTrue(exception.getMessage().contains("is not booted"));
  }

  private static DeviceConfig iosSimulator() {
    return new DeviceConfig(
        TargetType.SIMULATOR, "iPhone 17 Pro", Optional.empty(), Optional.of("26.4"));
  }
}
