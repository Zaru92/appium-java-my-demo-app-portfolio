package pl.zaru.page;

import pl.zaru.testdata.TestPaymentCard;

public interface PaymentPage {

  boolean isLoaded();

  void fillPaymentDetails(TestPaymentCard paymentCard);

  void continueToOrderReview();
}
