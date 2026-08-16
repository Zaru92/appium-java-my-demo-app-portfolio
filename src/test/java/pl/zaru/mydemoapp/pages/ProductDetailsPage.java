package pl.zaru.mydemoapp.pages;

public interface ProductDetailsPage {

  boolean isLoaded();

  String displayedProductName();

  void addToCart();

  CartPage openCart();
}
