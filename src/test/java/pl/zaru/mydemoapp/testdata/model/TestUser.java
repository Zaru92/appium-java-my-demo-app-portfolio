package pl.zaru.mydemoapp.testdata.model;

public record TestUser(String username, String password) {

  public TestUser {
    requireNonBlank(username, "username");
    requireNonBlank(password, "password");
  }

  private static void requireNonBlank(String value, String fieldName) {

    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
  }
}
