package pl.zaru.mydemoapp.pages.contracts;

import pl.zaru.mydemoapp.testdata.model.TestProduct;

public interface ProductCatalogPage {

  boolean isLoaded();

  ProductDetailsPage openProduct(TestProduct product);
}
