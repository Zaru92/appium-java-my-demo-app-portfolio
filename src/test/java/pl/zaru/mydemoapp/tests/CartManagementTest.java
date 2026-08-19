package pl.zaru.mydemoapp.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.pages.contracts.CartPage;
import pl.zaru.mydemoapp.pages.contracts.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.contracts.ProductDetailsPage;
import pl.zaru.mydemoapp.pages.factories.ProductCatalogPageFactory;
import pl.zaru.mydemoapp.testdata.TestProduct;

public final class CartManagementTest extends BaseTest {

  @Test
  public void shouldUpdateQuantityAndRemoveProduct() {
    String productName = TestProduct.BACKPACK.nameFor(driver());

    ProductCatalogPage catalogPage = ProductCatalogPageFactory.create(driver());

    ProductDetailsPage detailsPage = catalogPage.openProduct(productName);

    detailsPage.addToCart();

    CartPage cartPage = detailsPage.openCart();

    assertTrue(cartPage.isLoaded(), "Cart should be loaded.");

    assertEquals(cartPage.firstProductQuantity(), 1, "Initial product quantity should be one.");

    cartPage.increaseFirstProductQuantity();

    assertEquals(cartPage.firstProductQuantity(), 2, "Product quantity should increase to two.");

    cartPage.removeFirstProduct();

    assertTrue(cartPage.isEmpty(), "Cart should be empty after removing the product.");
  }
}
