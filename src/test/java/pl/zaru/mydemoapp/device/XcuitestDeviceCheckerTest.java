package pl.zaru.mydemoapp.device;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.config.DeviceConfig;
import pl.zaru.mydemoapp.config.TargetType;

public final class XcuitestDeviceCheckerTest {
  private static final String UDID = "00008150-00015084140A401C";

  @Test
  public void shouldAcceptRealIosDeviceWithActiveTunnel() {
    AtomicReference<URI> requestedUri = new AtomicReference<>();

    XcuitestDeviceChecker checker =
        new XcuitestDeviceChecker(
            (uri, timeout) -> {
              requestedUri.set(uri);

              return new XcuitestDeviceChecker.TunnelRegistryResponse(
                  200,
                  "{\"status\":\"OK\",\"tunnels\":{\"" + UDID + "\":{\"udid\":\"" + UDID + "\"}}}");
            });

    checker.verify(realDevice());

    assertEquals(requestedUri.get(), URI.create("http://127.0.0.1:42314/remotexpc/tunnels"));
  }

  @Test
  public void shouldRejectRealIosDeviceWithoutActiveTunnel() {
    XcuitestDeviceChecker checker =
        new XcuitestDeviceChecker(
            (uri, timeout) ->
                new XcuitestDeviceChecker.TunnelRegistryResponse(
                    200, "{\"status\":\"OK\",\"tunnels\":{}}"));

    IllegalStateException exception =
        expectThrows(IllegalStateException.class, () -> checker.verify(realDevice()));

    assertTrue(exception.getMessage().contains("No active Remote XPC tunnel"));
    assertTrue(exception.getMessage().contains(UDID));
  }

  @Test
  public void shouldRejectUnavailableTunnelRegistry() {
    XcuitestDeviceChecker checker =
        new XcuitestDeviceChecker(
            (uri, timeout) ->
                new XcuitestDeviceChecker.TunnelRegistryResponse(
                    503, "tunnel registry unavailable"));

    IllegalStateException exception =
        expectThrows(IllegalStateException.class, () -> checker.verify(realDevice()));

    assertTrue(exception.getMessage().contains("HTTP 503"));
  }

  private static DeviceConfig realDevice() {
    return new DeviceConfig(
        TargetType.REAL_DEVICE, "iPhone 17 #2", Optional.of(UDID), Optional.of("26.6"));
  }
}
