package pl.zaru.mydemoapp.device;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.TargetType;

public final class AdbDeviceChecker implements DeviceChecker {
  private static final Duration COMMAND_TIMEOUT = Duration.ofSeconds(10);

  private final CommandExecutor commandExecutor;

  public AdbDeviceChecker() {
    this(new SystemCommandExecutor());
  }

  AdbDeviceChecker(CommandExecutor commandExecutor) {
    this.commandExecutor =
        Objects.requireNonNull(commandExecutor, "commandExecutor must not be null");
  }

  @Override
  public void verify(DeviceConfig device) {
    Objects.requireNonNull(device, "device must not be null");

    if (device.targetType() == TargetType.SIMULATOR) {
      throw new IllegalArgumentException("ADB cannot verify an iOS simulator.");
    }

    String udid =
        device
            .udid()
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "udid must be provided to verify an Android target."));

    CommandResult state = execute(List.of("adb", "-s", udid, "get-state"), "ADB state check", udid);

    if (!state.output().equals("device")) {
      throw new IllegalStateException(
          "Android target %s is not ready. Expected ADB state 'device', but was '%s'."
              .formatted(udid, displayOutput(state)));
    }

    CommandResult bootState =
        execute(
            List.of("adb", "-s", udid, "shell", "getprop", "sys.boot_completed"),
            "Android boot check",
            udid);

    if (!bootState.output().equals("1")) {
      throw new IllegalStateException(
          "Android target %s has not completed booting. Received '%s'."
              .formatted(udid, displayOutput(bootState)));
    }
  }

  private CommandResult execute(List<String> command, String checkName, String udid) {
    CommandResult result = commandExecutor.execute(command, COMMAND_TIMEOUT);

    if (!result.successful()) {
      throw new IllegalStateException(
          "%s failed for %s with exit code %d: %s"
              .formatted(checkName, udid, result.exitCode(), displayOutput(result)));
    }

    return result;
  }

  private static String displayOutput(CommandResult result) {
    return result.output().isBlank() ? "<empty>" : result.output();
  }
}
