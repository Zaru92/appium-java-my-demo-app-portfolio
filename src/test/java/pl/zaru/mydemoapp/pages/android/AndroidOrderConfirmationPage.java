package pl.zaru.mydemoapp.pages.android;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.PageContext;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.OrderConfirmationPage;

public final class AndroidOrderConfirmationPage extends BasePage implements OrderConfirmationPage {

  private static final String APP_ID = "com.saucelabs.mydemoapp.android:id/";

  private static final By CONFIRMATION_HEADING = AppiumBy.id(APP_ID + "completeTV");

  private static final By CONFIRMATION_MESSAGE = AppiumBy.id(APP_ID + "thankYouTV");

  public AndroidOrderConfirmationPage(PageContext context) {
    super(context);
  }

  @Override
  public boolean isLoaded() {
    return isVisible(CONFIRMATION_HEADING);
  }

  @Override
  public String confirmationMessage() {
    return waitUntilVisible(CONFIRMATION_MESSAGE).getText();
  }
}
