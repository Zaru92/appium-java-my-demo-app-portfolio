package pl.zaru.mydemoapp.pages.contracts;

public interface ProductCatalogPage {

  boolean isLoaded();

  ProductDetailsPage openProduct(String productName);
}
