package pl.zaru.page.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.OrderReviewPage;

public final class IosOrderReviewPage extends BasePage
    implements OrderReviewPage {

    private static final By REVIEW_ORDER_HEADING =
        AppiumBy.iOSNsPredicateString(
            "type == 'XCUIElementTypeStaticText' "
                + "AND label == 'Review your order'");

    private static final By PLACE_ORDER_BUTTON =
        AppiumBy.accessibilityId("Place Order");

    public IosOrderReviewPage(AppiumDriver driver) {
        super(driver);
    }

    @Override
    public boolean isLoaded() {
        return waitUntilVisible(REVIEW_ORDER_HEADING).isDisplayed();
    }

    @Override
    public void placeOrder() {
        tap(PLACE_ORDER_BUTTON);
    }
}
