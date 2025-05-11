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

@DisplayName("For the payment requests received page")
public class PaymentRequestsReceivedPageTests extends WebTestRunner {
    private final WebSession session = new WebSession(PaymentRequestsReceivedPageTests.this);

    @Override
    protected void setupTestData() {
        PersonDAO personDAO = ServiceRegistry.lookup(PersonDAO.class);
        ExpenseDAO expenseDAO = ServiceRegistry.lookup(ExpenseDAO.class);

        Person firstTimeUser = new Person("firsttimeuser@mockemail.com");
        Person student1 = new Person("student1@mockemail.com");
        Person student2 = new Person("student2@mockemail.com");
        Stream.of(firstTimeUser, student1, student2).forEach(personDAO::savePerson);

        Expense expense1 = new Expense(student1, "Lunch", amountOf(300), TODAY);
        expense1.requestPayment(student2, amountOf(100), TOMORROW);
        Stream.of(expense1).forEach(expenseDAO::save);
    }

    @Test
    @DisplayName("when I not received any payment requests")
    void noPaymentRequestsReceived() throws IOException {
        session.openLoginPage()
                .login("firsttimeuser@mockemail.com")
                .shouldBeOnExpensesPage()
                .clickOnPaymentRequestsReceived()
                .shouldBeOnPaymentRequestsReceivedPage()
                .shouldHaveNoPaymentRequestsReceived()
                .takeScreenshot("paymentrequests-received");
    }

    @Test
    @DisplayName("when I have received a few payment requests")
    public void havePaymentRequestsReceived() throws IOException {
        session.openLoginPage()
                .login("student2@mockemail.com")
                .shouldBeOnExpensesPage()
                .takeScreenshot("expenses-before")
                .clickOnPaymentRequestsReceived()
                .shouldBeOnPaymentRequestsReceivedPage()
                .takeScreenshot("paymentrequests_received")
                .paymentRequestsReceivedGrandTotalShouldBe(amountOf(100))
                .takeScreenshot("paymentrequests_received");
    }

    @Test
    @DisplayName("I can pay a payment request that I received")
    public void payPaymentRequestReceived() throws IOException {
        session.openLoginPage()
                .login("student2@mockemail.com")
                .shouldBeOnExpensesPage()
                .takeScreenshot("expenses-before")
                .clickOnPaymentRequestsReceived()
                .shouldBeOnPaymentRequestsReceivedPage()
                .takeScreenshot("paymentrequests_received-paid")
                .payPaymentRequest()
                .shouldBeOnPaymentRequestsReceivedPage()
                .paymentRequestsReceivedShouldBePaid()
                .takeScreenshot("paymentrequests_received-paid")
                .clickOnExpensesLinkOnPaymentRequestsReceivedPage()
                .shouldBeOnExpensesPage()
                .expensesGrandTotalShouldBe(amountOf(100))
                .takeScreenshot("paymentrequests_received");
    }
}
