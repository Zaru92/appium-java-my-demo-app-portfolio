package pl.zaru.mydemoapp.pages.contracts;

public interface ProductDetailsPage {

  boolean isLoaded();

  String displayedProductName();

  void addToCart();

  CartPage openCart();
}
