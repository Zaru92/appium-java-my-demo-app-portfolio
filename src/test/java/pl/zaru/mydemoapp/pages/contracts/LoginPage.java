package pl.zaru.mydemoapp.pages.contracts;

public interface LoginPage extends LoadablePage {

  void login(String username, String password);

  boolean isValidationDisplayed(LoginValidation validation);
}
