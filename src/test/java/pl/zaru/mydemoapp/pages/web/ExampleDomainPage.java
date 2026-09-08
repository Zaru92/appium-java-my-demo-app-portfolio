package pl.zaru.mydemoapp.pages.web;

import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.PageContext;
import pl.zaru.mydemoapp.pages.base.BasePage;

public final class ExampleDomainPage extends BasePage {

  private static final By HEADING = By.cssSelector("h1");

  public ExampleDomainPage(PageContext context) {
    super(context);
  }

  public boolean isLoaded() {
    return isVisible(HEADING);
  }

  public String heading() {
    return waitUntilVisible(HEADING).getText();
  }
}
