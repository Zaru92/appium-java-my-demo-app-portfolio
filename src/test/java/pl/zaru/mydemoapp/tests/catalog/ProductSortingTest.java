package pl.zaru.mydemoapp.tests.catalog;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.pages.ScreenFactory;
import pl.zaru.mydemoapp.pages.contracts.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.contracts.ProductSort;
import pl.zaru.mydemoapp.tests.TestGroups;

@Epic("My Demo App")
@Feature("Catalog")
public final class ProductSortingTest extends BaseTest {

  @DataProvider(name = "productSorts")
  public Object[][] productSorts() {
    return new Object[][] {{ProductSort.NAME_ASCENDING}, {ProductSort.NAME_DESCENDING}};
  }

  @Story("Sort products by name")
  @Severity(SeverityLevel.NORMAL)
  @Test(
      dataProvider = "productSorts",
      groups = {TestGroups.REGRESSION, TestGroups.CATALOG})
  public void shouldSortVisibleProductsByName(ProductSort sort) {
    ProductCatalogPage catalogPage = new ScreenFactory(driver()).productCatalogPage();

    assertTrue(catalogPage.isLoaded(), "Product catalog should be displayed.");

    catalogPage.sortBy(sort);

    List<String> actualProductNames = catalogPage.visibleProductNames();

    assertTrue(
        actualProductNames.size() > 1,
        "At least two visible products are required to verify sorting.");

    List<String> expectedProductNames =
        actualProductNames.stream().sorted(sort.comparator()).toList();

    assertEquals(
        actualProductNames,
        expectedProductNames,
        "Visible products should be sorted using " + sort + ".");
  }
}
