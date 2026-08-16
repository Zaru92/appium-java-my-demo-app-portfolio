package pl.zaru.testdata;

public record TestPaymentCard(
    String fullName, String cardNumber, String expirationDate, String securityCode) {

  public static final TestPaymentCard DEFAULT =
      new TestPaymentCard("Maxim Winter", "4111111111111111", "0330", "123");

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
