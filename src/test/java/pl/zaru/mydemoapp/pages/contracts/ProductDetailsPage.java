package pl.zaru.mydemoapp.pages.contracts;

import pl.zaru.mydemoapp.testdata.model.TestProduct;

public interface ProductDetailsPage extends LoadablePage {

  boolean displaysProduct(TestProduct product);

  void addToCart();

  CartPage openCart();
}
