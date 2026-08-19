package pl.zaru.mydemoapp.testdata.model;

public record TestPaymentCard(
    String fullName, String cardNumber, String expirationDate, String securityCode) {

  public TestPaymentCard {
    requireNonBlank(fullName, "fullName");
    requireNonBlank(cardNumber, "cardNumber");
    requireNonBlank(expirationDate, "expirationDate");
    requireNonBlank(securityCode, "securityCode");
  }

  private static void requireNonBlank(String value, String fieldName) {

    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
  }
}
