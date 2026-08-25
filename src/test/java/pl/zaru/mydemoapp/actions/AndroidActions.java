package pl.zaru.mydemoapp.actions;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import java.util.Objects;

public final class AndroidActions {
  private final AndroidDriver driver;

  public AndroidActions(AppiumDriver driver) {
    Objects.requireNonNull(driver, "driver must not be null");

    if (!(driver instanceof AndroidDriver androidDriver)) {
      throw new IllegalArgumentException("AndroidActions requires an AndroidDriver.");
    }

    this.driver = androidDriver;
  }

  public void hideKeyboardIfPresent() {
    if (driver instanceof AndroidDriver androidDriver && androidDriver.isKeyboardShown()) {

      androidDriver.pressKey(new KeyEvent(AndroidKey.BACK));
    }
  }
}
