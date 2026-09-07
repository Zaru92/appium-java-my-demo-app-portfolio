package pl.zaru.mydemoapp.device;

import java.util.Objects;

record HttpProbeResponse(int statusCode, String body) {
  private static final int DISPLAY_LIMIT = 300;

  HttpProbeResponse {
    Objects.requireNonNull(body, "body must not be null");
  }

  String displayBody() {
    String normalized = body.replaceAll("\\s+", " ").trim();

    if (normalized.isEmpty()) {
      return "<empty>";
    }

    return normalized.length() <= DISPLAY_LIMIT
        ? normalized
        : normalized.substring(0, DISPLAY_LIMIT) + "...";
  }
}
