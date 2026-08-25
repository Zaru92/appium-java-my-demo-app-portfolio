package pl.zaru.mydemoapp.device;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public final class PortChecker {

  private static final String LOOPBACK_HOST = "127.0.0.1";

  public void verifyAvailable(String portName, int port) {
    try (ServerSocket socket = new ServerSocket()) {
      socket.setReuseAddress(false);
      socket.bind(new InetSocketAddress(LOOPBACK_HOST, port));
    } catch (IOException exception) {
      throw new IllegalStateException(
          "%s %d is not available on %s.".formatted(portName, port, LOOPBACK_HOST), exception);
    }
  }
}
