package pl.zaru.mydemoapp.pages.android;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.PageContext;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.OrderReviewPage;

public final class AndroidOrderReviewPage extends BasePage implements OrderReviewPage {

  private static final String APP_ID = "com.saucelabs.mydemoapp.android:id/";

  private static final By REVIEW_ORDER_HEADING = AppiumBy.id(APP_ID + "enterShippingAddressTV");

  private static final By PLACE_ORDER_BUTTON = AppiumBy.id(APP_ID + "paymentBtn");

  public AndroidOrderReviewPage(PageContext context) {
    super(context);
  }

  @Override
  public boolean isLoaded() {
    return isVisible(REVIEW_ORDER_HEADING);
  }

  @Override
  @Step("Place the order")
  public void placeOrder() {
    tap(PLACE_ORDER_BUTTON);
  }
}
