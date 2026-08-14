package pl.zaru.page;

public interface ProductCatalogPage {

  boolean isLoaded();

  ProductDetailsPage openProduct(String productName);
}
