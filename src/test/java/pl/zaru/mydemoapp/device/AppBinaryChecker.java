package pl.zaru.mydemoapp.device;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import pl.zaru.mydemoapp.config.TargetType;
import pl.zaru.mydemoapp.config.TestConfig;

public final class AppBinaryChecker implements PreflightCheck {

  @Override
  public void verify(TestConfig config) {
    Objects.requireNonNull(config, "config must not be null");

    Path appPath = config.appPath();

    if (!Files.exists(appPath)) {
      throw new IllegalStateException("Application binary does not exist: " + appPath);
    }

    if (!Files.isReadable(appPath)) {
      throw new IllegalStateException("Application binary is not readable: " + appPath);
    }

    switch (config.platform()) {
      case ANDROID -> verifyAndroidBinary(appPath);
      case IOS -> verifyIosBinary(appPath, config.device().targetType());
    }
  }

  private static void verifyAndroidBinary(Path appPath) {
    if (!Files.isRegularFile(appPath) || !fileName(appPath).endsWith(".apk")) {
      throw new IllegalStateException(
          "Android application must be a readable .apk file: " + appPath);
    }
  }

  private static void verifyIosBinary(Path appPath, TargetType targetType) {
    boolean validBinary =
        switch (targetType) {
          case SIMULATOR ->
              (Files.isRegularFile(appPath) && fileName(appPath).endsWith(".zip"))
                  || (Files.isDirectory(appPath) && fileName(appPath).endsWith(".app"));

          case REAL_DEVICE -> Files.isRegularFile(appPath) && fileName(appPath).endsWith(".ipa");

          case EMULATOR -> false;
        };

    if (!validBinary) {
      String expectedFormat =
          targetType == TargetType.SIMULATOR ? "a .zip file or .app directory" : "an .ipa file";

      throw new IllegalStateException(
          "iOS application must be %s: %s".formatted(expectedFormat, appPath));
    }
  }

  private static String fileName(Path path) {
    return path.getFileName().toString().toLowerCase(Locale.ROOT);
  }
}
