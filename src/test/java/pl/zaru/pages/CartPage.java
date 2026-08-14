package pl.zaru.page;

public interface CartPage {

  boolean isLoaded();

  boolean containsProduct(String productName);
}
