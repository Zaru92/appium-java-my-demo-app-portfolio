package pl.zaru.mydemoapp.device;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

@FunctionalInterface
interface HttpProbe {
  HttpProbeResponse get(URI uri, Duration timeout) throws IOException, InterruptedException;
}
