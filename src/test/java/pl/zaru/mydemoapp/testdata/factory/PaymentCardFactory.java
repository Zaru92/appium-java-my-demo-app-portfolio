package pl.zaru.mydemoapp.testdata.factory;

import pl.zaru.mydemoapp.testdata.model.TestPaymentCard;

public final class PaymentCardFactory {

  private PaymentCardFactory() {}

  public static TestPaymentCard validVisaCard() {
    return new TestPaymentCard("Maxim Winter", "4111111111111111", "0330", "123");
  }
}
