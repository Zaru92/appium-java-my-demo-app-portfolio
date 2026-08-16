package pl.zaru.page.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.OrderReviewPage;

public final class AndroidOrderReviewPage extends BasePage
    implements OrderReviewPage {

    private static final String APP_ID =
        "com.saucelabs.mydemoapp.android:id/";

    private static final By REVIEW_ORDER_HEADING =
        AppiumBy.id(APP_ID + "enterShippingAddressTV");

    private static final By PLACE_ORDER_BUTTON =
        AppiumBy.id(APP_ID + "paymentBtn");

    public AndroidOrderReviewPage(AppiumDriver driver) {
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
