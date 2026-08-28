package pl.zaru.mydemoapp.pages.contracts;

public interface LoginPage {

  boolean isLoaded();

  void login(String username, String password);

  boolean isValidationDisplayed(LoginValidation validation);
}
