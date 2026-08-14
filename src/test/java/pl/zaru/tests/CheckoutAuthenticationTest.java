package pl.zaru.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;
import pl.zaru.base.BaseTest;
import pl.zaru.page.CartPage;
import pl.zaru.page.ProductCatalogPage;
import pl.zaru.page.ProductCatalogPageFactory;
import pl.zaru.page.ProductDetailsPage;
import pl.zaru.page.LoginPage;
import pl.zaru.page.LoginPageFactory;
import pl.zaru.testdata.TestProduct;

public final class CheckoutAuthenticationTest extends BaseTest {

    @Test
    public void shouldRequireLoginBeforeCheckout() {
        String productName = TestProduct.BACKPACK.nameFor(driver());

        ProductCatalogPage catalogPage =
            ProductCatalogPageFactory.create(driver());

        ProductDetailsPage detailsPage =
            catalogPage.openProduct(productName);

        detailsPage.addToCart();

        CartPage cartPage = detailsPage.openCart();

        assertTrue(
            cartPage.isLoaded(),
            "Cart should be displayed."
        );

        cartPage.proceedToCheckout();

        LoginPage loginPage = LoginPageFactory.create(driver());

        assertTrue(
            loginPage.isLoaded(),
            "Unauthenticated user should be redirected to login before checkout."
        );
    }
}
