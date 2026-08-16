package pl.zaru.mydemoapp.pages;

import pl.zaru.mydemoapp.testdata.TestPaymentCard;

public interface PaymentPage {

  boolean isLoaded();

  void fillPaymentDetails(TestPaymentCard paymentCard);

  void continueToOrderReview();
}
