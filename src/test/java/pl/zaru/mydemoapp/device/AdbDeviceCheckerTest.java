package pl.zaru.mydemoapp.device;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.TargetType;

public final class AdbDeviceCheckerTest {

  @Test
  public void shouldAcceptReadyAndroidDevice() {
    AdbDeviceChecker checker =
        new AdbDeviceChecker(returning(new CommandResult(0, "device"), new CommandResult(0, "1")));

    checker.verify(androidDevice());
  }

  @Test
  public void shouldRejectOfflineAndroidDevice() {
    AdbDeviceChecker checker = new AdbDeviceChecker(returning(new CommandResult(0, "offline")));

    IllegalStateException exception =
        expectThrows(IllegalStateException.class, () -> checker.verify(androidDevice()));

    assertTrue(exception.getMessage().contains("is not ready"));
  }

  private static DeviceConfig androidDevice() {
    return new DeviceConfig(
        TargetType.EMULATOR, "Pixel_8", Optional.of("emulator-5554"), Optional.empty());
  }

  private static CommandExecutor returning(CommandResult... commandResults) {
    Queue<CommandResult> results = new ArrayDeque<>(List.of(commandResults));

    return (command, timeout) -> results.remove();
  }
}
