package pl.zaru.mydemoapp.pages;

public interface LoginPage {

  boolean isLoaded();

  void login(String username, String password);
}
