package pl.zaru.mydemoapp.device;

import java.time.Duration;
import java.util.List;

@FunctionalInterface
interface CommandExecutor {
  CommandResult execute(List<String> command, Duration timeout);
}
