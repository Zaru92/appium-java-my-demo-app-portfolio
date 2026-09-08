package pl.zaru.mydemoapp.system;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class SystemCommandExecutor implements CommandExecutor {

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

    Process process;

    try {
      process = new ProcessBuilder(command).redirectErrorStream(true).start();
    } catch (IOException exception) {
      throw new IllegalStateException(
          "Could not execute command: " + String.join(" ", command), exception);
    }

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      Future<String> outputReader =
          executor.submit(() -> new String(process.getInputStream().readAllBytes(), UTF_8));

      boolean completed = process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);

      if (!completed) {
        process.destroyForcibly();
        process.waitFor();

        throw new IllegalStateException(
            "Command timed out after %d seconds: %s"
                .formatted(timeout.toSeconds(), String.join(" ", command)));
      }

      String output = outputReader.get().trim();

      return new CommandResult(process.exitValue(), output);
    } catch (InterruptedException exception) {
      process.destroyForcibly();
      Thread.currentThread().interrupt();
      throw new IllegalStateException(
          "Command execution was interrupted: " + String.join(" ", command), exception);
    } catch (ExecutionException exception) {
      process.destroyForcibly();
      throw new IllegalStateException(
          "Could not read output from command: " + String.join(" ", command), exception);
    }
  }
}
