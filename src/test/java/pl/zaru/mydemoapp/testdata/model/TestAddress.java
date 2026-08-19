package pl.zaru.mydemoapp.testdata.model;

public record TestAddress(
    String fullName,
    String addressLine1,
    String addressLine2,
    String city,
    String state,
    String zipCode,
    String country) {}
