package pl.zaru.mydemoapp.testdata.model;

import java.util.Objects;

/** Form input; empty values are allowed so tests can exercise application validation. */
public record TestPaymentCard(
    String fullName, String cardNumber, String expirationDate, String securityCode) {

  public TestPaymentCard {
    Objects.requireNonNull(fullName, "fullName must not be null");
    Objects.requireNonNull(cardNumber, "cardNumber must not be null");
    Objects.requireNonNull(expirationDate, "expirationDate must not be null");
    Objects.requireNonNull(securityCode, "securityCode must not be null");
  }
}
