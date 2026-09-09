package pl.zaru.mydemoapp.pages.ios;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.PageContext;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.OrderReviewPage;

public final class IosOrderReviewPage extends BasePage implements OrderReviewPage {

  private static final By REVIEW_ORDER_HEADING =
      AppiumBy.iOSNsPredicateString(
          "type == 'XCUIElementTypeStaticText' " + "AND label == 'Review your order'");

  private static final By PLACE_ORDER_BUTTON = AppiumBy.accessibilityId("Place Order");

  public IosOrderReviewPage(PageContext context) {
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
