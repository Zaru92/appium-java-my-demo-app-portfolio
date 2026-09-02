package pl.zaru.mydemoapp.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.SupportsContextSwitching;
import java.time.Duration;
import java.util.Objects;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class ContextManager {

  private static final Duration DEFAULT_CONTEXT_TIMEOUT = Duration.ofSeconds(15);

  private static final String NATIVE_CONTEXT = "NATIVE_APP";

  private static final String WEBVIEW_CONTEXT_PREFIX = "WEBVIEW_";

  private final SupportsContextSwitching contextDriver;
  private final Duration contextTimeout;
  private final WebDriverWait wait;

  public ContextManager(AppiumDriver driver) {
    this(driver, DEFAULT_CONTEXT_TIMEOUT);
  }

  ContextManager(AppiumDriver driver, Duration contextTimeout) {
    Objects.requireNonNull(driver, "driver must not be null");
    this.contextTimeout = Objects.requireNonNull(contextTimeout, "contextTimeout must not be null");

    if (!(driver instanceof SupportsContextSwitching supportsContextSwitching)) {
      throw new IllegalArgumentException(
          "Driver does not support native and web context switching.");
    }

    if (contextTimeout.isZero() || contextTimeout.isNegative()) {
      throw new IllegalArgumentException("contextTimeout must be positive");
    }

    contextDriver = supportsContextSwitching;

    wait = new WebDriverWait(driver, contextTimeout);
    wait.ignoring(WebDriverException.class);
  }

  public String switchToWebView() {
    try {
      String webViewContext = wait.until(ignored -> findWebViewContext());

      contextDriver.context(webViewContext);
      return webViewContext;
    } catch (TimeoutException exception) {
      throw new IllegalStateException(
          "No WEBVIEW context became available within "
              + contextTimeout.toSeconds()
              + " seconds. Available contexts: "
              + contextDriver.getContextHandles(),
          exception);
    }
  }

  public void switchToNative() {
    contextDriver.context(NATIVE_CONTEXT);
  }

  private String findWebViewContext() {
    return contextDriver.getContextHandles().stream()
        .filter(context -> context.startsWith(WEBVIEW_CONTEXT_PREFIX))
        .findFirst()
        .orElse(null);
  }
}
