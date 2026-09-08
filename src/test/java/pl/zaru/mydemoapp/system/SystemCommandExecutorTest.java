package pl.zaru.mydemoapp.system;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import org.testng.annotations.Test;

public final class SystemCommandExecutorTest {

  private static final int LARGE_OUTPUT_LENGTH = 256 * 1024;

  private final SystemCommandExecutor commandExecutor = new SystemCommandExecutor();

  @Test
  public void shouldReadOutputLargerThanProcessPipeBuffer() {
    CommandResult result = commandExecutor.execute(javaCommand("output"), Duration.ofSeconds(10));

    assertTrue(result.successful());
    assertEquals(result.output().length(), LARGE_OUTPUT_LENGTH);
    assertTrue(result.output().chars().allMatch(character -> character == 'x'));
  }

  @Test
  public void shouldTerminateCommandAfterTimeout() {
    IllegalStateException exception =
        expectThrows(
            IllegalStateException.class,
            () -> commandExecutor.execute(javaCommand("sleep"), Duration.ofMillis(100)));

    assertTrue(exception.getMessage().startsWith("Command timed out after"));
  }

  private static List<String> javaCommand(String action) {
    String javaExecutable = Path.of(System.getProperty("java.home"), "bin", "java").toString();

    return List.of(
        javaExecutable,
        "-cp",
        System.getProperty("java.class.path"),
        TestProcess.class.getName(),
        action);
  }

  public static final class TestProcess {

    private TestProcess() {}

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String[] arguments) throws InterruptedException {
      if (arguments.length != 1) {
        throw new IllegalArgumentException("Exactly one action is required.");
      }

      switch (arguments[0]) {
        case "output" -> System.out.print("x".repeat(LARGE_OUTPUT_LENGTH));
        case "sleep" -> Thread.sleep(Duration.ofSeconds(5));
        default -> throw new IllegalArgumentException("Unknown action: " + arguments[0]);
      }
    }
  }
}
