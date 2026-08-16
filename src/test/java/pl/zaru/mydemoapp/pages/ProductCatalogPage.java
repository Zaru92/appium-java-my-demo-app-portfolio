package pl.zaru.mydemoapp.pages;

public interface ProductCatalogPage {

  boolean isLoaded();

  ProductDetailsPage openProduct(String productName);
}
