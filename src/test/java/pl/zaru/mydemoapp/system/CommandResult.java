package pl.zaru.mydemoapp.system;

import java.util.Objects;

public record CommandResult(int exitCode, String output) {

  public CommandResult {
    Objects.requireNonNull(output, "output must not be null");
  }

  public boolean successful() {
    return exitCode == 0;
  }

  public String displayOutput() {
    return output.isBlank() ? "<empty>" : output;
  }
}
