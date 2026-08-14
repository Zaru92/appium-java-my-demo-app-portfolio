package pl.zaru.page;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import pl.zaru.page.android.AndroidLoginPage;
import pl.zaru.page.ios.IosLoginPage;

public final class LoginPageFactory {

    private LoginPageFactory() {
    }

    public static LoginPage create(AppiumDriver driver) {
        if (driver instanceof AndroidDriver) {
            return new AndroidLoginPage(driver);
        }

        if (driver instanceof IOSDriver) {
            return new IosLoginPage(driver);
        }

        throw new IllegalArgumentException(
            "Unsupported driver type: " + driver.getClass().getName()
        );
    }
}
