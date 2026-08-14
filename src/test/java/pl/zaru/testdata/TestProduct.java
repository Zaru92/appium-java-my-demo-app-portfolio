package pl.zaru.testdata;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebDriver;

public enum TestProduct {
  BACKPACK("Sauce Labs Backpack", "Sauce Labs Backpack - Black");

  private final String androidName;
  private final String iosName;

  TestProduct(String androidName, String iosName) {
    this.androidName = androidName;
    this.iosName = iosName;
  }

  public String nameFor(WebDriver driver) {
    if (driver instanceof AndroidDriver) {
      return androidName;
    }

    if (driver instanceof IOSDriver) {
      return iosName;
    }

    throw new IllegalArgumentException("Unsupported driver type: " + driver.getClass().getName());
  }
}
