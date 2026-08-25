package pl.zaru.mydemoapp.device;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.TargetType;

public final class SimctlDeviceChecker implements DeviceChecker {
  private static final Duration COMMAND_TIMEOUT = Duration.ofSeconds(10);

  private final CommandExecutor commandExecutor;

  public SimctlDeviceChecker() {
    this(new SystemCommandExecutor());
  }

  SimctlDeviceChecker(CommandExecutor commandExecutor) {
    this.commandExecutor =
        Objects.requireNonNull(commandExecutor, "commandExecutor must not be null");
  }

  @Override
  public void verify(DeviceConfig device) {
    Objects.requireNonNull(device, "device must not be null");

    if (device.targetType() != TargetType.SIMULATOR) {
      throw new IllegalArgumentException("SimctlDeviceChecker supports only iOS simulators.");
    }

    CommandResult result =
        commandExecutor.execute(
            List.of("xcrun", "simctl", "list", "devices", "booted"), COMMAND_TIMEOUT);

    if (!result.successful()) {
      throw new IllegalStateException(
          "Could not list booted iOS simulators. Exit code %d: %s"
              .formatted(result.exitCode(), displayOutput(result)));
    }

    boolean simulatorAvailable =
        result.output().lines().map(String::trim).anyMatch(line -> matches(device, line));

    if (!simulatorAvailable) {
      throw new IllegalStateException(
          "iOS simulator '%s' is not booted.".formatted(device.deviceName()));
    }
  }

  private static boolean matches(DeviceConfig device, String line) {
    if (!line.endsWith("(Booted)")) {
      return false;
    }

    return device
        .udid()
        .map(udid -> line.contains("(" + udid + ")"))
        .orElseGet(() -> line.startsWith(device.deviceName() + " ("));
  }

  private static String displayOutput(CommandResult result) {
    return result.output().isBlank() ? "<empty>" : result.output();
  }
}
