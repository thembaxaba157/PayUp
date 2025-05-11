package payup.webtests;

import java.io.IOException;

/*
 ** DO NOT CHANGE!!
 */

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import payup.model.Person;
import payup.persistence.ExpenseDAO;
import payup.persistence.PersonDAO;
import payup.server.ServiceRegistry;
import payup.webtests.pages.ExpensesPage;

@DisplayName("When using the app")
public class LoginAndLogoutTests extends WebTestRunner {
    private final WebSession session = new WebSession(LoginAndLogoutTests.this);

    @Override
    protected void setupTestData() {
        PersonDAO personDAO = ServiceRegistry.lookup(PersonDAO.class);
        ExpenseDAO expenseDAO = ServiceRegistry.lookup(ExpenseDAO.class);

        Person p = new Person("student@mockemail.com");
        personDAO.savePerson(p);
    }

    @Test
    @DisplayName("I am asked to login before viewing any other page")
    void redirectWhenNotLoggedIn() throws IOException {
        session.open(new ExpensesPage(LoginAndLogoutTests.this))
                .shouldBeOnLoginPage()
                .takeScreenshot("login");
    }

    @Test
    @DisplayName("I logged in and can see the home page")
    void successfulLogin() throws IOException {
        session.openLoginPage()
                .login("student@mockemail.com")
                .shouldBeOnExpensesPage()
                .shouldHaveEmailAddressDisplayed()
                .shouldHaveLogoutLinkDisplayed()
                .takeScreenshot("home");
    }

    @Test
    @DisplayName("I can logout after logging in")
    void logout() throws IOException {
        session.openLoginPage()
                .takeScreenshot("login")
                .login("student@mockemail.com")
                .shouldBeOnExpensesPage()
                .takeScreenshot("home")
                .logout()
                .shouldBeOnLoginPage()
                .takeScreenshot("logged-out");
    }
}
