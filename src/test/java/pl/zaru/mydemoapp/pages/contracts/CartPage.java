package pl.zaru.mydemoapp.pages.contracts;

import pl.zaru.mydemoapp.testdata.model.TestProduct;

public interface CartPage {

  boolean isLoaded();

  boolean containsProduct(TestProduct product);

  int firstProductQuantity();

  void increaseFirstProductQuantity();

  void removeFirstProduct();

  boolean isEmpty();

  void proceedToCheckout();
}
