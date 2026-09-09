package pl.zaru.mydemoapp.pages.contracts;

import java.util.List;
import pl.zaru.mydemoapp.testdata.model.TestProduct;

public interface ProductCatalogPage extends LoadablePage {

  List<String> visibleProductNames();

  void sortBy(ProductSort sort);

  ProductDetailsPage openProduct(TestProduct product);
}
