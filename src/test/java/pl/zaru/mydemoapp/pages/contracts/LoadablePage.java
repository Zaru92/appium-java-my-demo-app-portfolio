package pl.zaru.mydemoapp.pages.contracts;

@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface LoadablePage {

  boolean isLoaded();

  default void waitUntilLoaded() {
    if (!isLoaded()) {
      throw new IllegalStateException(getClass().getSimpleName() + " did not become visible.");
    }
  }
}
