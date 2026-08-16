package pl.zaru.mydemoapp.pages;

public interface CartPage {

  boolean isLoaded();

  boolean containsProduct(String productName);

  int firstProductQuantity();

  void increaseFirstProductQuantity();

  void removeFirstProduct();

  boolean isEmpty();

  void proceedToCheckout();
}
