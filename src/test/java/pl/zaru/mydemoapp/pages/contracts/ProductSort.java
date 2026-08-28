package pl.zaru.mydemoapp.pages.contracts;

import java.util.Comparator;

public enum ProductSort {
  NAME_ASCENDING(Comparator.naturalOrder()),
  NAME_DESCENDING(Comparator.reverseOrder());

  private final Comparator<String> comparator;

  ProductSort(Comparator<String> comparator) {
    this.comparator = comparator;
  }

  public Comparator<String> comparator() {
    return comparator;
  }

  public ProductSort opposite() {
    return switch (this) {
      case NAME_ASCENDING -> NAME_DESCENDING;
      case NAME_DESCENDING -> NAME_ASCENDING;
    };
  }
}
