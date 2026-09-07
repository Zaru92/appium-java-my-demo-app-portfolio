package pl.zaru.mydemoapp.testdata.factory;

import pl.zaru.mydemoapp.testdata.model.TestPaymentCard;

public final class PaymentCardFactory {

  private PaymentCardFactory() {}

  public static TestPaymentCard validVisaCard() {
    return new TestPaymentCard("Maxim Winter", "4111111111111111", "0330", "123");
  }

  public static TestPaymentCard paymentCardWithoutNumber() {
    TestPaymentCard validCard = validVisaCard();

    return new TestPaymentCard(
        validCard.fullName(), "", validCard.expirationDate(), validCard.securityCode());
  }
}
