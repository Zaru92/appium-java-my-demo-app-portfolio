package pl.zaru.mydemoapp.testdata.model;

public enum TestProduct {
  BACKPACK("Sauce Labs Backpack", "Sauce Labs Backpack - Black");

  private final String androidName;
  private final String iosName;

  TestProduct(String androidName, String iosName) {
    this.androidName = requireNonBlank(androidName, "androidName");
    this.iosName = requireNonBlank(iosName, "iosName");
  }

  public String androidName() {
    return androidName;
  }

  public String iosName() {
    return iosName;
  }

  private static String requireNonBlank(String value, String fieldName) {

    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }

    return value;
  }
}
