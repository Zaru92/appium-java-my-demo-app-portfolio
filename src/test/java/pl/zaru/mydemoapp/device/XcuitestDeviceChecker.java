package pl.zaru.mydemoapp.device;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.regex.Pattern;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.TargetType;

public final class XcuitestDeviceChecker implements DeviceChecker {
  private static final URI TUNNEL_REGISTRY_URI =
      URI.create("http://127.0.0.1:42314/remotexpc/tunnels");

  private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);

  private final TunnelRegistryClient tunnelRegistryClient;

  public XcuitestDeviceChecker() {
    this(new JavaTunnelRegistryClient());
  }

  XcuitestDeviceChecker(TunnelRegistryClient tunnelRegistryClient) {
    this.tunnelRegistryClient =
        Objects.requireNonNull(tunnelRegistryClient, "tunnelRegistryClient must not be null");
  }

  @Override
  public void verify(DeviceConfig device) {
    Objects.requireNonNull(device, "device must not be null");

    if (device.targetType() != TargetType.REAL_DEVICE) {
      throw new IllegalArgumentException("XcuitestDeviceChecker supports only real iOS devices.");
    }

    String udid =
        device
            .udid()
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "udid must be provided to verify a real iOS device."));

    TunnelRegistryResponse response =
        tunnelRegistryClient.get(TUNNEL_REGISTRY_URI, REQUEST_TIMEOUT);

    if (response.statusCode() < 200 || response.statusCode() >= 300) {
      throw new IllegalStateException(
          "Remote XPC tunnel registry returned HTTP %d: %s"
              .formatted(response.statusCode(), displayBody(response.body())));
    }

    Pattern deviceTunnelPattern = Pattern.compile("\"" + Pattern.quote(udid) + "\"\\s*:\\s*\\{");

    if (!deviceTunnelPattern.matcher(response.body()).find()) {
      throw new IllegalStateException(
          "No active Remote XPC tunnel found for real iOS device %s at %s."
              .formatted(udid, TUNNEL_REGISTRY_URI));
    }
  }

  private static String displayBody(String body) {
    String normalized = body.replaceAll("\\s+", " ").trim();

    if (normalized.isEmpty()) {
      return "<empty>";
    }

    return normalized.length() <= 300 ? normalized : normalized.substring(0, 300) + "...";
  }

  @FunctionalInterface
  interface TunnelRegistryClient {
    TunnelRegistryResponse get(URI registryUri, Duration timeout);
  }

  record TunnelRegistryResponse(int statusCode, String body) {
    TunnelRegistryResponse {
      Objects.requireNonNull(body, "body must not be null");
    }
  }

  private static final class JavaTunnelRegistryClient implements TunnelRegistryClient {
    private final HttpClient httpClient =
        HttpClient.newBuilder().connectTimeout(REQUEST_TIMEOUT).build();

    @Override
    public TunnelRegistryResponse get(URI registryUri, Duration timeout) {
      HttpRequest request = HttpRequest.newBuilder(registryUri).timeout(timeout).GET().build();

      try {
        HttpResponse<String> response =
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return new TunnelRegistryResponse(response.statusCode(), response.body());
      } catch (InterruptedException exception) {
        Thread.currentThread().interrupt();

        throw new IllegalStateException(
            "Remote XPC tunnel request was interrupted: " + registryUri, exception);
      } catch (IOException exception) {
        throw new IllegalStateException(
            "Remote XPC tunnel registry is not reachable at " + registryUri + ".", exception);
      }
    }
  }
}
