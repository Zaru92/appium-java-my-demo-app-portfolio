package pl.zaru.tests;

import org.testng.annotations.Test;
import pl.zaru.base.BaseTest;
import pl.zaru.page.CartPage;
import pl.zaru.page.ProductCatalogPage;
import pl.zaru.page.ProductCatalogPageFactory;
import pl.zaru.page.ProductDetailsPage;
import pl.zaru.testdata.TestProduct;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public final class CartManagementTest extends BaseTest {

    @Test
    public void shouldUpdateQuantityAndRemoveProduct() {
        String productName = TestProduct.BACKPACK.nameFor(driver());

        ProductCatalogPage catalogPage =
            ProductCatalogPageFactory.create(driver());

        ProductDetailsPage detailsPage =
            catalogPage.openProduct(productName);

        detailsPage.addToCart();

        CartPage cartPage = detailsPage.openCart();

        assertTrue(
            cartPage.isLoaded(),
            "Cart should be loaded."
        );

        assertEquals(
            cartPage.firstProductQuantity(),
            1,
            "Initial product quantity should be one."
        );

        cartPage.increaseFirstProductQuantity();

        assertEquals(
            cartPage.firstProductQuantity(),
            2,
            "Product quantity should increase to two."
        );

        cartPage.removeFirstProduct();

        assertTrue(
            cartPage.isEmpty(),
            "Cart should be empty after removing the product."
        );
    }
}
