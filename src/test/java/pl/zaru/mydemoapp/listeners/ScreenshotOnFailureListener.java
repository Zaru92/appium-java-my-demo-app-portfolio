package pl.zaru.mydemoapp.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import pl.zaru.mydemoapp.driver.DriverManager;
import pl.zaru.mydemoapp.reporting.FailureArtifacts;

public final class ScreenshotOnFailureListener implements IInvokedMethodListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotOnFailureListener.class);

  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult result) {
    if (!method.isTestMethod() || result.getStatus() != ITestResult.FAILURE) {
      return;
    }

    if (!DriverManager.hasSession()) {
      LOGGER.warn(
          "Could not capture failure artifacts for {} because no Appium session is active.",
          result.getMethod().getQualifiedName());

      return;
    }

    String testName =
        result.getTestClass().getRealClass().getSimpleName()
            + "."
            + result.getMethod().getMethodName();

    FailureArtifacts.capture(DriverManager.getDriver(), testName);
  }
}
