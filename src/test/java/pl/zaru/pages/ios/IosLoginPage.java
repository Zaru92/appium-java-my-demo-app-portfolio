package pl.zaru.page.ios;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.LoginPage;

public final class IosLoginPage extends BasePage implements LoginPage {

    private static final By LOGIN_TITLE =
        AppiumBy.iOSNsPredicateString(
            "type == 'XCUIElementTypeStaticText' AND label == 'Login'"
        );

    public IosLoginPage(AppiumDriver driver) {
        super(driver);
    }

    @Override
    public boolean isLoaded() {
        return waitUntilVisible(LOGIN_TITLE).isDisplayed();
    }
}
