package payup.webtests;

import static payup.model.DateHelper.*;
import static payup.model.MoneyHelper.*;

import java.io.IOException;
import java.util.stream.Stream;

/*
 ** DO NOT CHANGE!!
 */

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import payup.model.Expense;
import payup.model.Person;
import payup.persistence.ExpenseDAO;
import payup.persistence.PersonDAO;
import payup.server.ServiceRegistry;

@DisplayName("For an expense")
public class PaymentRequestFormTests extends WebTestRunner {

    private final WebSession session = new WebSession(PaymentRequestFormTests.this);

    @Override
    protected void setupTestData() {
        PersonDAO personDAO = ServiceRegistry.lookup(PersonDAO.class);
        ExpenseDAO expenseDAO = ServiceRegistry.lookup(ExpenseDAO.class);

        Person p1 = new Person("student1@mockemail.com");
        Person p2 = new Person("student2@mockemail.com");
        Person p3 = new Person("student3@mockemail.com");
        Stream.of(p1, p2, p3).forEach(personDAO::savePerson);

        Expense expense1 = new Expense(p1, "Lunch", amountOf(300), TODAY);
        Expense expense2 = new Expense(p1, "Airtime", amountOf(100), TODAY);
        expense1.requestPayment(p2, amountOf(100), TOMORROW);
        Stream.of(expense1, expense2).forEach(expenseDAO::save);
    }

    @Test
    @DisplayName("I can view the page to capture a payment request")
    public void noPaymentRequests() throws IOException {
        session.openLoginPage()
                .login("student1@mockemail.com")
                .shouldBeOnExpensesPage()
                .clickOnExpense("Airtime")
                .shouldBeOnPaymentRequestPage("Airtime")
                .takeScreenshot("payment-request")
                .clickOnExpensesLinkOnPaymentRequestPage()
                .shouldBeOnExpensesPage();
    }

    @Test
    @DisplayName("I can see prior payment requests")
    public void hasPriorPaymentRequests() throws IOException {
        session.openLoginPage()
                .login("student1@mockemail.com")
                .shouldBeOnExpensesPage()
                .clickOnExpense("Lunch")
                .shouldBeOnPaymentRequestPage("Lunch")
                .takeScreenshot("payment-request")
                .shouldHavePaymentRequest("Student2")
                .clickOnExpensesLinkOnPaymentRequestPage()
                .shouldBeOnExpensesPage();
    }

    @Test
    @DisplayName("I can submit a payment request")
    public void capturePaymentRequest() throws IOException {
        session.openLoginPage()
                .login("student1@mockemail.com")
                .shouldBeOnExpensesPage()
                .clickOnExpense("Lunch")
                .shouldBeOnPaymentRequestPage("Lunch")
                .takeScreenshot("payment-request-before-capture")
                .shouldHavePaymentRequest("Student2")
                .fillPaymentRequestForm("student3@mockemail.com", amountOf(100), TOMORROW)
                .submitPaymentRequestForm()
                .takeScreenshot("payment-request-form-filled")
                .shouldBeOnPaymentRequestPage("Lunch")
                .shouldHavePaymentRequest("Student2")
                .shouldHavePaymentRequest("Student3")
                .takeScreenshot("payment-request-after-capture");
    }
}
