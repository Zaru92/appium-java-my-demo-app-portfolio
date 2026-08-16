package pl.zaru.page.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.OrderConfirmationPage;

public final class IosOrderConfirmationPage extends BasePage
    implements OrderConfirmationPage {

    private static final By CONFIRMATION_HEADING =
        AppiumBy.iOSNsPredicateString(
            "type == 'XCUIElementTypeStaticText' "
                + "AND label == 'Checkout Complete'");

    private static final By CONFIRMATION_MESSAGE =
        AppiumBy.iOSNsPredicateString(
            "type == 'XCUIElementTypeStaticText' "
                + "AND label BEGINSWITH 'Thank you for your order'");

    public IosOrderConfirmationPage(AppiumDriver driver) {
        super(driver);
    }

    @Override
    public boolean isLoaded() {
        return waitUntilVisible(CONFIRMATION_HEADING).isDisplayed();
    }

    @Override
    public String confirmationMessage() {
        return waitUntilVisible(CONFIRMATION_MESSAGE).getText();
    }
}
