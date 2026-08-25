package pl.zaru.mydemoapp.device;

import java.util.Objects;

record CommandResult(int exitCode, String output) {

  CommandResult {
    Objects.requireNonNull(output, "output must not be null");
  }

  boolean successful() {
    return exitCode == 0;
  }
}
