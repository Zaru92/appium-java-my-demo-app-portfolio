package pl.zaru.mydemoapp.testdata;

public enum TestUser {
  STANDARD("bob@example.com", "10203040");

  private final String username;
  private final String password;

  TestUser(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String username() {
    return username;
  }

  public String password() {
    return password;
  }
}
