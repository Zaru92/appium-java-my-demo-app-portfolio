package pl.zaru.mydemoapp.device;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;

final class JavaHttpProbe implements HttpProbe {
  private final HttpClient httpClient;

  JavaHttpProbe(Duration connectTimeout) {
    Objects.requireNonNull(connectTimeout, "connectTimeout must not be null");

    if (connectTimeout.isZero() || connectTimeout.isNegative()) {
      throw new IllegalArgumentException("connectTimeout must be positive");
    }

    httpClient = HttpClient.newBuilder().connectTimeout(connectTimeout).build();
  }

  @Override
  public HttpProbeResponse get(URI uri, Duration timeout) throws IOException, InterruptedException {
    Objects.requireNonNull(uri, "uri must not be null");
    Objects.requireNonNull(timeout, "timeout must not be null");

    HttpRequest request = HttpRequest.newBuilder(uri).timeout(timeout).GET().build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    return new HttpProbeResponse(response.statusCode(), response.body());
  }
}
