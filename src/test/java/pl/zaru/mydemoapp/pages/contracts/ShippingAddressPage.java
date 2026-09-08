package pl.zaru.mydemoapp.pages.contracts;

import pl.zaru.mydemoapp.testdata.model.TestAddress;

public interface ShippingAddressPage extends LoadablePage {

  void fillAddress(TestAddress address);

  void continueToPayment();

  boolean isValidationDisplayed(ShippingAddressValidation validation);

  /** Closes modal validation feedback; inline errors remain visible. */
  void dismissValidationIfPresent();

  /** Checks the form after submission without requiring the heading to be in view. */
  boolean isFormDisplayed();
}
