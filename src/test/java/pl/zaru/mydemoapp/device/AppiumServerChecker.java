package pl.zaru.mydemoapp.device;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.regex.Pattern;
import pl.zaru.mydemoapp.config.TestConfig;

public final class AppiumServerChecker implements PreflightCheck {
  private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);

  private static final Pattern READY_PATTERN =
      Pattern.compile("\"ready\"\\s*:\\s*true", Pattern.CASE_INSENSITIVE);

  private final StatusClient statusClient;

  public AppiumServerChecker() {
    this(new JavaStatusClient());
  }

  AppiumServerChecker(StatusClient statusClient) {
    this.statusClient = Objects.requireNonNull(statusClient, "statusClient must not be null");
  }

  @Override
  public void verify(TestConfig config) {
    Objects.requireNonNull(config, "config must not be null");

    URI statusUri = createStatusUri(config.appiumUrl());
    StatusResponse response = statusClient.get(statusUri, REQUEST_TIMEOUT);

    if (response.statusCode() < 200 || response.statusCode() >= 300) {
      throw new IllegalStateException(
          "Appium status endpoint returned HTTP %d: %s"
              .formatted(response.statusCode(), displayBody(response.body())));
    }

    if (!READY_PATTERN.matcher(response.body()).find()) {
      throw new IllegalStateException(
          "Appium server at %s is not ready. Response: %s"
              .formatted(statusUri, displayBody(response.body())));
    }
  }

  static URI createStatusUri(URI appiumUrl) {
    String baseUrl = appiumUrl.toString();

    return URI.create(baseUrl.endsWith("/") ? baseUrl + "status" : baseUrl + "/status");
  }

  private static String displayBody(String body) {
    String normalized = body.replaceAll("\\s+", " ").trim();

    if (normalized.isEmpty()) {
      return "<empty>";
    }

    return normalized.length() <= 300 ? normalized : normalized.substring(0, 300) + "...";
  }

  @FunctionalInterface
  interface StatusClient {
    StatusResponse get(URI statusUri, Duration timeout);
  }

  record StatusResponse(int statusCode, String body) {
    StatusResponse {
      Objects.requireNonNull(body, "body must not be null");
    }
  }

  private static final class JavaStatusClient implements StatusClient {
    private final HttpClient httpClient =
        HttpClient.newBuilder().connectTimeout(REQUEST_TIMEOUT).build();

    @Override
    public StatusResponse get(URI statusUri, Duration timeout) {
      HttpRequest request = HttpRequest.newBuilder(statusUri).timeout(timeout).GET().build();

      try {
        HttpResponse<String> response =
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return new StatusResponse(response.statusCode(), response.body());
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
}
