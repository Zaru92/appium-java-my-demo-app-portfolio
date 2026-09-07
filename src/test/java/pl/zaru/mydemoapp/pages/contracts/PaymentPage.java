package pl.zaru.mydemoapp.pages.contracts;

import pl.zaru.mydemoapp.testdata.model.TestPaymentCard;

public interface PaymentPage {

  boolean isLoaded();

  void fillPaymentDetails(TestPaymentCard paymentCard);

  void continueToOrderReview();

  boolean isValidationDisplayed(PaymentValidation validation);

  /** Closes modal validation feedback; inline errors remain visible. */
  void dismissValidationIfPresent();

  /** Checks the form after submission without requiring the heading to be in view. */
  boolean isFormDisplayed();
}
