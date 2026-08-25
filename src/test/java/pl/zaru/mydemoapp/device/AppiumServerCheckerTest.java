package pl.zaru.mydemoapp.device;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.Platform;
import pl.zaru.mydemoapp.config.TargetType;
import pl.zaru.mydemoapp.config.TestConfig;

public final class AppiumServerCheckerTest {

  @Test
  public void shouldAcceptReadyAppiumServer() {
    AtomicReference<URI> requestedUri = new AtomicReference<>();

    AppiumServerChecker checker =
        new AppiumServerChecker(
            (uri, timeout) -> {
              requestedUri.set(uri);

              return new AppiumServerChecker.StatusResponse(200, "{\"value\":{\"ready\":true}}");
            });

    checker.verify(config());

    assertEquals(requestedUri.get(), URI.create("http://127.0.0.1:4723/status"));
  }

  @Test
  public void shouldRejectAppiumServerThatIsNotReady() {
    AppiumServerChecker checker =
        new AppiumServerChecker(
            (uri, timeout) ->
                new AppiumServerChecker.StatusResponse(200, "{\"value\":{\"ready\":false}}"));

    IllegalStateException exception =
        expectThrows(IllegalStateException.class, () -> checker.verify(config()));

    assertTrue(exception.getMessage().contains("is not ready"));
  }

  private static TestConfig config() {
    return new TestConfig(
        URI.create("http://127.0.0.1:4723"),
        Platform.ANDROID,
        new DeviceConfig(
            TargetType.EMULATOR, "Pixel_8", Optional.of("emulator-5554"), Optional.empty()),
        Optional.empty(),
        Path.of("application.apk"),
        Duration.ofSeconds(120));
  }
}
