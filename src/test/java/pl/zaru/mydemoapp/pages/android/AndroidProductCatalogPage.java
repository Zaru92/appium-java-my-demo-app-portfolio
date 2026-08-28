package pl.zaru.mydemoapp.pages.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import java.util.List;
import java.util.Objects;
import org.openqa.selenium.By;
import pl.zaru.mydemoapp.pages.base.BasePage;
import pl.zaru.mydemoapp.pages.contracts.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.contracts.ProductDetailsPage;
import pl.zaru.mydemoapp.pages.contracts.ProductSort;
import pl.zaru.mydemoapp.testdata.model.TestProduct;

public final class AndroidProductCatalogPage extends BasePage implements ProductCatalogPage {

  private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

  private static final String PRODUCT_NAME_ID = APP_PACKAGE + ":id/titleTV";

  private static final String PRODUCT_IMAGE_ID = APP_PACKAGE + ":id/productIV";

  private static final By PRODUCT_LIST = By.id(APP_PACKAGE + ":id/productRV");

  private static final By SORT_BUTTON = By.id(APP_PACKAGE + ":id/sortIV");

  private static final By NAME_ASCENDING_OPTION = By.id(APP_PACKAGE + ":id/nameAscCL");

  private static final By NAME_DESCENDING_OPTION = By.id(APP_PACKAGE + ":id/nameDesCL");

  public AndroidProductCatalogPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(PRODUCT_LIST).isDisplayed();
  }

  @Override
  public List<String> visibleProductNames() {
    return waitUntilVisibleElements(By.id(PRODUCT_NAME_ID)).stream()
        .map(element -> element.getText().trim())
        .filter(name -> !name.isBlank())
        .toList();
  }

  @Override
  public void sortBy(ProductSort sort) {
    By sortOption =
        switch (Objects.requireNonNull(sort, "sort must not be null")) {
          case NAME_ASCENDING -> NAME_ASCENDING_OPTION;
          case NAME_DESCENDING -> NAME_DESCENDING_OPTION;
        };

    tap(SORT_BUTTON);
    tap(sortOption);
  }

  @Override
  public ProductDetailsPage openProduct(TestProduct product) {
    String productName = Objects.requireNonNull(product, "product must not be null").androidName();

    String escapedProductName = productName.replace("\\", "\\\\").replace("\"", "\\\"");

    By productImage =
        AppiumBy.androidUIAutomator(
            ("new UiSelector().resourceId(\"%s\").text(\"%s\")"
                    + ".fromParent(new UiSelector().resourceId(\"%s\"))")
                .formatted(PRODUCT_NAME_ID, escapedProductName, PRODUCT_IMAGE_ID));

    tap(productImage);

    return new AndroidProductDetailsPage(driver());
  }
}
