package pl.zaru.mydemoapp.device;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Objects;
import java.util.regex.Pattern;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.TargetType;

public final class XcuitestDeviceChecker implements DeviceChecker {
  private static final URI TUNNEL_REGISTRY_URI =
      URI.create("http://127.0.0.1:42314/remotexpc/tunnels");

  private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);

  private final HttpProbe httpProbe;

  public XcuitestDeviceChecker() {
    this(new JavaHttpProbe(REQUEST_TIMEOUT));
  }

  XcuitestDeviceChecker(HttpProbe httpProbe) {
    this.httpProbe = Objects.requireNonNull(httpProbe, "httpProbe must not be null");
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

    HttpProbeResponse response = requestTunnelRegistry();

    if (response.statusCode() < 200 || response.statusCode() >= 300) {
      throw new IllegalStateException(
          "Remote XPC tunnel registry returned HTTP %d: %s"
              .formatted(response.statusCode(), response.displayBody()));
    }

    Pattern deviceTunnelPattern = Pattern.compile("\"" + Pattern.quote(udid) + "\"\\s*:\\s*\\{");

    if (!deviceTunnelPattern.matcher(response.body()).find()) {
      throw new IllegalStateException(
          "No active Remote XPC tunnel found for real iOS device %s at %s."
              .formatted(udid, TUNNEL_REGISTRY_URI));
    }
  }

  private HttpProbeResponse requestTunnelRegistry() {
    try {
      return httpProbe.get(TUNNEL_REGISTRY_URI, REQUEST_TIMEOUT);
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException(
          "Remote XPC tunnel request was interrupted: " + TUNNEL_REGISTRY_URI, exception);
    } catch (IOException exception) {
      throw new IllegalStateException(
          "Remote XPC tunnel registry is not reachable at " + TUNNEL_REGISTRY_URI + ".", exception);
    }
  }
}
