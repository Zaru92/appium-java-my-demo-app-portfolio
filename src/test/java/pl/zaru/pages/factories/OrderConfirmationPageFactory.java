package pl.zaru.page;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import java.util.Objects;
import pl.zaru.page.android.AndroidOrderConfirmationPage;
import pl.zaru.page.ios.IosOrderConfirmationPage;

public final class OrderConfirmationPageFactory {

    private OrderConfirmationPageFactory() {}

    public static OrderConfirmationPage create(AppiumDriver driver) {
        Objects.requireNonNull(driver, "driver must not be null");

        if (driver instanceof AndroidDriver) {
            return new AndroidOrderConfirmationPage(driver);
        }

        if (driver instanceof IOSDriver) {
            return new IosOrderConfirmationPage(driver);
        }

        throw new IllegalArgumentException(
            "Unsupported driver type: " + driver.getClass().getName());
    }
}
