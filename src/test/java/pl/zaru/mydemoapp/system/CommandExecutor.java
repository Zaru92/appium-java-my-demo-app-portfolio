package pl.zaru.mydemoapp.system;

import java.time.Duration;
import java.util.List;

@FunctionalInterface
public interface CommandExecutor {
  CommandResult execute(List<String> command, Duration timeout);
}
