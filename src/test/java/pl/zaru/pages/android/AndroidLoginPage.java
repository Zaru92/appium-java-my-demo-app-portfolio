package pl.zaru.page.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pl.zaru.page.BasePage;
import pl.zaru.page.LoginPage;

public final class AndroidLoginPage extends BasePage implements LoginPage {

    private static final String APP_PACKAGE =
        "com.saucelabs.mydemoapp.android";

    private static final By LOGIN_TITLE =
        AppiumBy.id(APP_PACKAGE + ":id/loginTV");

    public AndroidLoginPage(AppiumDriver driver) {
        super(driver);
    }

    @Override
    public boolean isLoaded() {
        return waitUntilVisible(LOGIN_TITLE).isDisplayed();
    }
}
