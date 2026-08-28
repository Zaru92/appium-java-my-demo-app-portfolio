package pl.zaru.mydemoapp.pages.ios;

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

public final class IosProductCatalogPage extends BasePage implements ProductCatalogPage {

  private static final By CATALOG_SCREEN = AppiumBy.accessibilityId("Catalog-screen");

  private static final By PRODUCT_NAMES = AppiumBy.accessibilityId("Product Name");

  private static final By SORT_BUTTON =
      AppiumBy.iOSNsPredicateString(
          "type == 'XCUIElementTypeButton' "
              + "AND name == 'Button' "
              + "AND label == 'Button' "
              + "AND visible == 1");

  private static final By NAME_ASCENDING_OPTION = AppiumBy.accessibilityId("Name - Ascending");

  private static final By NAME_DESCENDING_OPTION = AppiumBy.accessibilityId("Name - Descending");

  public IosProductCatalogPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public boolean isLoaded() {
    return waitUntilVisible(CATALOG_SCREEN).isDisplayed();
  }

  @Override
  public List<String> visibleProductNames() {
    return waitUntilVisibleElements(PRODUCT_NAMES).stream()
        .map(element -> element.getAttribute("label"))
        .filter(Objects::nonNull)
        .map(String::trim)
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
    String productName = Objects.requireNonNull(product, "product must not be null").iosName();

    String escapedProductName = productName.replace("\\", "\\\\").replace("'", "\\'");

    By productNameLabel =
        AppiumBy.iOSNsPredicateString(
            "type == 'XCUIElementTypeStaticText' " + "AND label == '" + escapedProductName + "'");

    tap(productNameLabel);

    return new IosProductDetailsPage(driver());
  }
}
