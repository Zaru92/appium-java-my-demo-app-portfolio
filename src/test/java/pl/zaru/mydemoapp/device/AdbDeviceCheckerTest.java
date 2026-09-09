package pl.zaru.mydemoapp.device;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.TargetType;
import pl.zaru.mydemoapp.system.CommandExecutor;
import pl.zaru.mydemoapp.system.CommandResult;

public final class AdbDeviceCheckerTest {

  private static final String DEVICE_UDID = "emulator-5554";

  @Test
  public void shouldAcceptReadyAndroidDevice() {
    RecordingCommandExecutor commandExecutor =
        new RecordingCommandExecutor(new CommandResult(0, "device"), new CommandResult(0, "1"));
    AdbDeviceChecker checker = new AdbDeviceChecker(commandExecutor);

    checker.verify(androidDevice());

    assertEquals(
        commandExecutor.commands(),
        List.of(
            List.of("adb", "-s", DEVICE_UDID, "get-state"),
            List.of("adb", "-s", DEVICE_UDID, "shell", "getprop", "sys.boot_completed")));
    assertEquals(
        commandExecutor.timeouts(), List.of(Duration.ofSeconds(10), Duration.ofSeconds(10)));
  }

  @Test
  public void shouldRejectOfflineAndroidDevice() {
    RecordingCommandExecutor commandExecutor =
        new RecordingCommandExecutor(new CommandResult(0, "offline"));
    AdbDeviceChecker checker = new AdbDeviceChecker(commandExecutor);

    IllegalStateException exception =
        expectThrows(IllegalStateException.class, () -> checker.verify(androidDevice()));

    assertTrue(exception.getMessage().contains("is not ready"));
    assertEquals(
        commandExecutor.commands(), List.of(List.of("adb", "-s", DEVICE_UDID, "get-state")));
  }

  private static DeviceConfig androidDevice() {
    return new DeviceConfig(
        TargetType.EMULATOR, "Pixel_8", Optional.of(DEVICE_UDID), Optional.empty());
  }

  private static final class RecordingCommandExecutor implements CommandExecutor {
    private final Queue<CommandResult> results;
    private final List<List<String>> commands = new ArrayList<>();
    private final List<Duration> timeouts = new ArrayList<>();

    private RecordingCommandExecutor(CommandResult... commandResults) {
      results = new ArrayDeque<>(List.of(commandResults));
    }

    @Override
    public CommandResult execute(List<String> command, Duration timeout) {
      commands.add(List.copyOf(command));
      timeouts.add(timeout);

      return results.remove();
    }

    private List<List<String>> commands() {
      return List.copyOf(commands);
    }

    private List<Duration> timeouts() {
      return List.copyOf(timeouts);
    }
  }
}
