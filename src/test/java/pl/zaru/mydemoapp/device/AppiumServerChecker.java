package pl.zaru.mydemoapp.device;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Objects;
import java.util.regex.Pattern;
import pl.zaru.mydemoapp.config.TestConfig;

public final class AppiumServerChecker implements PreflightCheck {
  private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);

  private static final Pattern READY_PATTERN =
      Pattern.compile("\"ready\"\\s*:\\s*true", Pattern.CASE_INSENSITIVE);

  private final HttpProbe httpProbe;

  public AppiumServerChecker() {
    this(new JavaHttpProbe(REQUEST_TIMEOUT));
  }

  AppiumServerChecker(HttpProbe httpProbe) {
    this.httpProbe = Objects.requireNonNull(httpProbe, "httpProbe must not be null");
  }

  @Override
  public void verify(TestConfig config) {
    Objects.requireNonNull(config, "config must not be null");

    URI statusUri = createStatusUri(config.appiumUrl());
    HttpProbeResponse response = requestStatus(statusUri);

    if (response.statusCode() < 200 || response.statusCode() >= 300) {
      throw new IllegalStateException(
          "Appium status endpoint returned HTTP %d: %s"
              .formatted(response.statusCode(), response.displayBody()));
    }

    if (!READY_PATTERN.matcher(response.body()).find()) {
      throw new IllegalStateException(
          "Appium server at %s is not ready. Response: %s"
              .formatted(statusUri, response.displayBody()));
    }
  }

  static URI createStatusUri(URI appiumUrl) {
    String baseUrl = appiumUrl.toString();

    return URI.create(baseUrl.endsWith("/") ? baseUrl + "status" : baseUrl + "/status");
  }

  private HttpProbeResponse requestStatus(URI statusUri) {
    try {
      return httpProbe.get(statusUri, REQUEST_TIMEOUT);
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException(
          "Appium status request was interrupted: " + statusUri, exception);
    } catch (IOException exception) {
      throw new IllegalStateException(
          "Appium server is not reachable at " + statusUri + ".", exception);
    }
  }
}
