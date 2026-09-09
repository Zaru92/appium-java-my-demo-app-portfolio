package pl.zaru.mydemoapp.pages.contracts;

public interface WebViewPage extends LoadablePage {

  void openUrl(String url);

  void returnToUrlForm();
}
