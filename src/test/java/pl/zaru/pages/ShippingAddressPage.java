package pl.zaru.page;

import pl.zaru.testdata.TestAddress;

public interface ShippingAddressPage {

  boolean isLoaded();

  void fillAddress(TestAddress address);

  void continueToPayment();
}
