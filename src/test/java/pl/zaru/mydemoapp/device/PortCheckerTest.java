package pl.zaru.mydemoapp.device;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.expectThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import org.testng.annotations.Test;

public class PortCheckerTest {

  private final PortChecker portChecker = new PortChecker();

  @Test
  public void shouldAcceptAvailablePort() throws IOException {
    int availablePort;

    try (ServerSocket socket = new ServerSocket()) {
      socket.bind(new InetSocketAddress("127.0.0.1", 0));
      availablePort = socket.getLocalPort();
    }

    portChecker.verifyAvailable("systemPort", availablePort);
  }

  @Test
  public void shouldRejectOccupiedPort() throws IOException {
    try (ServerSocket occupiedSocket = new ServerSocket()) {
      occupiedSocket.setReuseAddress(false);
      occupiedSocket.bind(new InetSocketAddress("127.0.0.1", 0));

      int occupiedPort = occupiedSocket.getLocalPort();

      IllegalStateException exception =
          expectThrows(
              IllegalStateException.class,
              () -> portChecker.verifyAvailable("systemPort", occupiedPort));

      assertEquals(
          exception.getMessage(),
          "systemPort %d is not available on 127.0.0.1.".formatted(occupiedPort));
    }
  }
}
