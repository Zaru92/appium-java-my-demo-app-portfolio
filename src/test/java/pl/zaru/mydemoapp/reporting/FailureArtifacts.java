package pl.zaru.mydemoapp.reporting;

import static java.nio.charset.StandardCharsets.UTF_8;

import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FailureArtifacts {
  private static final Logger LOGGER = LoggerFactory.getLogger(FailureArtifacts.class);

  private static final Path OUTPUT_DIRECTORY = Path.of("target", "failure-artifacts");

  private static final DateTimeFormatter TIMESTAMP_FORMAT =
      DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS").withZone(ZoneOffset.UTC);

  private FailureArtifacts() {}

  public static void capture(AppiumDriver driver, String testName) {
    Objects.requireNonNull(driver, "driver must not be null");

    String filePrefix =
        sanitize(testName)
            + "-"
            + TIMESTAMP_FORMAT.format(Instant.now())
            + "-t"
            + Thread.currentThread().threadId();

    captureScreenshot(driver, filePrefix);
    capturePageSource(driver, filePrefix);
  }

  @SuppressWarnings("PMD.AvoidCatchingGenericException")
  private static void captureScreenshot(AppiumDriver driver, String filePrefix) {

    try {
      byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);

      Files.createDirectories(OUTPUT_DIRECTORY);

      Path screenshotPath = OUTPUT_DIRECTORY.resolve(filePrefix + ".png");

      Files.write(screenshotPath, screenshot);

      Allure.addAttachment(
          "Screenshot on failure", "image/png", new ByteArrayInputStream(screenshot), ".png");

      LOGGER.info("Saved failure screenshot: {}", screenshotPath.toAbsolutePath());
    } catch (RuntimeException | IOException exception) {
      LOGGER.warn("Could not capture failure screenshot.", exception);
    }
  }

  @SuppressWarnings("PMD.AvoidCatchingGenericException")
  private static void capturePageSource(AppiumDriver driver, String filePrefix) {

    try {
      String pageSource = driver.getPageSource();

      Files.createDirectories(OUTPUT_DIRECTORY);

      Path pageSourcePath = OUTPUT_DIRECTORY.resolve(filePrefix + "-page-source.xml");

      Files.writeString(pageSourcePath, pageSource, UTF_8);

      Allure.addAttachment(
          "Page source on failure",
          "application/xml",
          new ByteArrayInputStream(pageSource.getBytes(UTF_8)),
          ".xml");

      LOGGER.info("Saved failure page source: {}", pageSourcePath.toAbsolutePath());
    } catch (RuntimeException | IOException exception) {
      LOGGER.warn("Could not capture failure page source.", exception);
    }
  }

  private static String sanitize(String testName) {
    if (testName == null || testName.isBlank()) {
      return "unknown-test";
    }

    return testName.replaceAll("[^a-zA-Z0-9._-]", "_");
  }
}
