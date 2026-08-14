package pl.zaru.page;

public interface LoginPage {

  boolean isLoaded();

  void login(String username, String password);
}
