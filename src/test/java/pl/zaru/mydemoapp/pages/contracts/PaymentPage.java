package pl.zaru.mydemoapp.pages.contracts;

import pl.zaru.mydemoapp.testdata.model.TestPaymentCard;

public interface PaymentPage {

  boolean isLoaded();

  void fillPaymentDetails(TestPaymentCard paymentCard);

  void continueToOrderReview();
}
