package pl.zaru.mydemoapp.tests.cart;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.pages.ScreenFactory;
import pl.zaru.mydemoapp.pages.contracts.CartPage;
import pl.zaru.mydemoapp.pages.contracts.ProductCatalogPage;
import pl.zaru.mydemoapp.pages.contracts.ProductDetailsPage;
import pl.zaru.mydemoapp.testdata.model.TestProduct;

public final class CartManagementTest extends BaseTest {

  @Test
  public void shouldUpdateQuantityAndRemoveProduct() {

    ScreenFactory screens = new ScreenFactory(driver());

    TestProduct product = TestProduct.BACKPACK;

    ProductCatalogPage catalogPage = screens.productCatalogPage();

    ProductDetailsPage detailsPage = catalogPage.openProduct(product);

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
