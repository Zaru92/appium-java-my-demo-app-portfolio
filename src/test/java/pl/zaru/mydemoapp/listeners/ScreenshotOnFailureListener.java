package pl.zaru.mydemoapp.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;
import pl.zaru.mydemoapp.driver.DriverManager;
import pl.zaru.mydemoapp.reporting.FailureArtifacts;

public final class ScreenshotOnFailureListener implements ITestListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotOnFailureListener.class);

  @Override
  public void onTestFailure(ITestResult result) {
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
