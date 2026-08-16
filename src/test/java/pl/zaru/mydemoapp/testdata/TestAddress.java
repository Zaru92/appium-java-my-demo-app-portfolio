package pl.zaru.mydemoapp.testdata;

public record TestAddress(
    String fullName,
    String addressLine1,
    String addressLine2,
    String city,
    String state,
    String zipCode,
    String country) {

  public static final TestAddress DEFAULT =
      new TestAddress(
          "Jan Kowalski", "Prosta 1", "Apartment 2", "Warsaw", "Mazowieckie", "00001", "Poland");
}
