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


@DisplayName("For the payment requests sent page")
public class PaymentRequestsSentPageTests extends WebTestRunner {
    private final WebSession session = new WebSession(PaymentRequestsSentPageTests.this);

    @Override
    protected void setupTestData() {
        PersonDAO personDAO = ServiceRegistry.lookup(PersonDAO.class);
        ExpenseDAO expenseDAO = ServiceRegistry.lookup(ExpenseDAO.class);

        Person firstTimeUser = new Person("firsttimeuser@mockemail.com");
        Person student1 = new Person("student1@mockemail.com");
        Person student2 = new Person("student2@mockemail.com");
        Person student3 = new Person("student3@mockemail.com");
        Stream.of(firstTimeUser, student1, student2, student3).forEach(personDAO::savePerson);

        Expense expense1 = new Expense(student1, "Lunch", amountOf(300), TODAY);
        expense1.requestPayment(student2, amountOf(100), TOMORROW);
        expense1.requestPayment(student3, amountOf(100), TOMORROW);
        expenseDAO.save(expense1);
    }

    @Test
    @DisplayName("when I sent no payment requests")
    void noPaymentRequests() throws IOException {
        session.openLoginPage()
                .login("firsttimeuser@mockemail.com")
                .shouldBeOnExpensesPage()
                .clickOnPaymentRequestsSent()
                .shouldBeOnPaymentRequestsSentPage()
                .shouldHaveNoPaymentRequestsSent()
                .takeScreenshot("paymentrequests-sent");
    }

    @Test
    @DisplayName("when I have a few payment requests sent")
    public void havePaymentRequestsSent() throws IOException {
        session.openLoginPage()
                .login("student1@mockemail.com")
                .shouldBeOnExpensesPage()
                .takeScreenshot("expenses-before")
                .clickOnPaymentRequestsSent()
                .shouldBeOnPaymentRequestsSentPage()
                .paymentRequestsSentGrandTotalShouldBe(amountOf(200))
                .takeScreenshot("paymentrequests_sent");
    }
}
