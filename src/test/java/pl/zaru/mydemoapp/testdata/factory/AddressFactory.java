package pl.zaru.mydemoapp.testdata.factory;

import pl.zaru.mydemoapp.testdata.model.TestAddress;

public final class AddressFactory {

  private AddressFactory() {}

  public static TestAddress validShippingAddress() {
    return new TestAddress(
        "Jan Kowalski", "Prosta 1", "Apartment 2", "Warsaw", "Mazowieckie", "00001", "Poland");
  }
}
