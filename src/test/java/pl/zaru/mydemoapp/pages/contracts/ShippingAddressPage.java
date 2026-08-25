package pl.zaru.mydemoapp.pages.contracts;

import pl.zaru.mydemoapp.testdata.model.TestAddress;

public interface ShippingAddressPage {

  boolean isLoaded();

  void fillAddress(TestAddress address);

  void continueToPayment();
}
