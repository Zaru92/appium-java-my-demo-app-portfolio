package pl.zaru.mydemoapp.tests;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.pages.CartPage;
import pl.zaru.mydemoapp.pages.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.ProductCatalogPageFactory;
import pl.zaru.mydemoapp.pages.ProductDetailsPage;
import pl.zaru.mydemoapp.testdata.TestProduct;

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
