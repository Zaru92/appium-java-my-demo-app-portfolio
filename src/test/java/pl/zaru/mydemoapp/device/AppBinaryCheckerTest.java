package pl.zaru.mydemoapp.device;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.Platform;
import pl.zaru.mydemoapp.config.TargetType;
import pl.zaru.mydemoapp.config.TestConfig;

public final class AppBinaryCheckerTest {

  @Test
  public void shouldAcceptAndroidApk() throws IOException {
    Path appPath = Files.createTempFile("my-demo-app-", ".apk");

    try {
      new AppBinaryChecker().verify(androidConfig(appPath));
    } finally {
      Files.deleteIfExists(appPath);
    }
  }

  @Test
  public void shouldRejectMissingApplication() {
    Path missingApp = Path.of("missing-application.apk");

    IllegalStateException exception =
        expectThrows(
            IllegalStateException.class,
            () -> new AppBinaryChecker().verify(androidConfig(missingApp)));

    assertTrue(exception.getMessage().contains("does not exist"));
  }

  private static TestConfig androidConfig(Path appPath) {
    return new TestConfig(
        URI.create("http://127.0.0.1:4723"),
        Platform.ANDROID,
        new DeviceConfig(
            TargetType.EMULATOR, "Pixel_8", Optional.of("emulator-5554"), Optional.empty()),
        Optional.empty(),
        appPath,
        Duration.ofSeconds(120));
  }
}
