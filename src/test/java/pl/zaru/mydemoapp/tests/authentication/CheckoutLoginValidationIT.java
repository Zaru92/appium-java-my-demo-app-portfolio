package pl.zaru.mydemoapp.tests.authentication;

import static org.testng.Assert.assertTrue;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import java.util.Objects;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pl.zaru.mydemoapp.base.BaseTest;
import pl.zaru.mydemoapp.flows.CheckoutFlow;
import pl.zaru.mydemoapp.pages.ScreenFactory;
import pl.zaru.mydemoapp.pages.contracts.LoginPage;
import pl.zaru.mydemoapp.pages.contracts.LoginValidation;
import pl.zaru.mydemoapp.testdata.factory.UserFactory;
import pl.zaru.mydemoapp.testdata.model.TestProduct;
import pl.zaru.mydemoapp.testdata.model.TestUser;
import pl.zaru.mydemoapp.tests.TestGroups;

@Epic("My Demo App")
@Feature("Authentication")
public final class CheckoutLoginValidationIT extends BaseTest {

  @DataProvider(name = "missingCredentials")
  public static Object[][] missingCredentials() {
    TestUser validUser = UserFactory.standardUser();

    return new Object[][] {
      {
        new LoginValidationCase(
            "missing username", "", validUser.password(), LoginValidation.USERNAME_REQUIRED)
      },
      {
        new LoginValidationCase(
            "missing password", validUser.username(), "", LoginValidation.PASSWORD_REQUIRED)
      }
    };
  }

  @Story("Validate required login credentials")
  @Severity(SeverityLevel.CRITICAL)
  @Test(
      dataProvider = "missingCredentials",
      groups = {TestGroups.REGRESSION, TestGroups.AUTHENTICATION})
  public void shouldRejectCheckoutLoginWhenRequiredCredentialIsMissing(
      LoginValidationCase testCase) {

    ScreenFactory screens = screenFactory();
    CheckoutFlow checkout = new CheckoutFlow(screens);

    LoginPage loginPage = checkout.openLoginFor(TestProduct.BACKPACK);

    loginPage.login(testCase.username(), testCase.password());

    assertTrue(
        loginPage.isValidationDisplayed(testCase.expectedValidation()),
        "Expected validation "
            + testCase.expectedValidation()
            + " for case: "
            + testCase.caseName());
  }

  private record LoginValidationCase(
      String caseName, String username, String password, LoginValidation expectedValidation) {

    private LoginValidationCase {
      if (Objects.requireNonNull(caseName, "caseName must not be null").isBlank()) {
        throw new IllegalArgumentException("caseName must not be blank");
      }

      Objects.requireNonNull(username, "username must not be null");
      Objects.requireNonNull(password, "password must not be null");
      Objects.requireNonNull(expectedValidation, "expectedValidation must not be null");
    }

    @Override
    public String toString() {
      return caseName;
    }
  }
}
