package pl.zaru.mydemoapp.device;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

final class SystemCommandExecutor implements CommandExecutor {

  @Override
  public CommandResult execute(List<String> command, Duration timeout) {
    Objects.requireNonNull(command, "command must not be null");
    Objects.requireNonNull(timeout, "timeout must not be null");

    if (command.isEmpty()) {
      throw new IllegalArgumentException("command must not be empty");
    }

    if (timeout.isZero() || timeout.isNegative()) {
      throw new IllegalArgumentException("timeout must be positive");
    }

    try {
      Process process = new ProcessBuilder(command).redirectErrorStream(true).start();

      boolean completed = process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);

      if (!completed) {
        process.destroyForcibly();

        throw new IllegalStateException(
            "Command timed out after %d seconds: %s"
                .formatted(timeout.toSeconds(), String.join(" ", command)));
      }

      String output = new String(process.getInputStream().readAllBytes(), UTF_8).trim();

      return new CommandResult(process.exitValue(), output);
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException(
          "Command execution was interrupted: " + String.join(" ", command), exception);
    } catch (IOException exception) {
      throw new IllegalStateException(
          "Could not execute command: " + String.join(" ", command), exception);
    }
  }
}
