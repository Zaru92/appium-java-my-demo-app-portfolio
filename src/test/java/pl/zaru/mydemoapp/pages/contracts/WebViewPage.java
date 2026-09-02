package pl.zaru.mydemoapp.pages.contracts;

public interface WebViewPage {

  boolean isLoaded();

  void openUrl(String url);

  void returnToUrlForm();
}
