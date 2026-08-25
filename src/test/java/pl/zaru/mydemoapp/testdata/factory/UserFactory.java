package pl.zaru.mydemoapp.testdata.factory;

import pl.zaru.mydemoapp.testdata.model.TestUser;

public final class UserFactory {

  private UserFactory() {}

  public static TestUser standardUser() {
    return new TestUser("bob@example.com", "10203040");
  }
}
