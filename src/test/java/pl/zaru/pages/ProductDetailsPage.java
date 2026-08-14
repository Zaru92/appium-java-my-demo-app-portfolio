package pl.zaru.page;

public interface ProductDetailsPage {

  boolean isLoaded();

  String displayedProductName();

  void addToCart();

  CartPage openCart();
}
