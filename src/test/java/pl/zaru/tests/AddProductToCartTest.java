package pl.zaru.tests;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;
import pl.zaru.base.BaseTest;
import pl.zaru.page.CartPage;
import pl.zaru.page.ProductCatalogPage;
import pl.zaru.page.ProductCatalogPageFactory;
import pl.zaru.page.ProductDetailsPage;
import pl.zaru.testdata.TestProduct;

public final class AddProductToCartTest extends BaseTest {

  @Test
  public void shouldAddSelectedProductToCart() {
    String productName = TestProduct.BACKPACK.nameFor(driver());

    ProductCatalogPage catalogPage = ProductCatalogPageFactory.create(driver());

    assertTrue(catalogPage.isLoaded(), "Product catalog should be loaded.");

    ProductDetailsPage detailsPage = catalogPage.openProduct(productName);

    assertTrue(detailsPage.isLoaded(), "Product details should be loaded.");

    detailsPage.addToCart();

    CartPage cartPage = detailsPage.openCart();

    assertTrue(cartPage.isLoaded(), "Cart should be loaded.");

    assertTrue(
        cartPage.containsProduct(productName),
        "Cart should contain selected product: " + productName);
  }
}
