package pl.zaru.mydemoapp.pages.web;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.base.BasePage;

public final class ExampleDomainPage extends BasePage {

  private static final By HEADING = By.cssSelector("h1");

  public ExampleDomainPage(AppiumDriver driver) {
    super(driver);
  }

  public boolean isLoaded() {
    return waitUntilVisible(HEADING).isDisplayed();
  }

  public String heading() {
    return waitUntilVisible(HEADING).getText();
  }
}
