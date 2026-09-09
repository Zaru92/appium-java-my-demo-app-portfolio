package pl.zaru.mydemoapp.listeners;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public final class DevicePreflightListenerTest {

  private static final String TESTNG_DRY_RUN_PROPERTY = "testng.mode.dryrun";

  @Test
  public void shouldSkipPreflightCallbacksDuringDiscoveryDryRun() {
    String originalValue = System.getProperty(TESTNG_DRY_RUN_PROPERTY);

    try {
      System.setProperty(TESTNG_DRY_RUN_PROPERTY, Boolean.TRUE.toString());

      DevicePreflightListener listener = new DevicePreflightListener();
      listener.onStart(null);
      listener.onFinish(null);

      assertTrue(Boolean.getBoolean(TESTNG_DRY_RUN_PROPERTY));
    } finally {
      restoreSystemProperty(originalValue);
    }
  }

  private static void restoreSystemProperty(String originalValue) {
    if (originalValue == null) {
      System.clearProperty(TESTNG_DRY_RUN_PROPERTY);
    } else {
      System.setProperty(TESTNG_DRY_RUN_PROPERTY, originalValue);
    }
  }
}
