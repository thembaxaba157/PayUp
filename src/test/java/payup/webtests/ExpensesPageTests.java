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
import payup.model.PaymentRequest;
import payup.model.Person;
import payup.persistence.ExpenseDAO;
import payup.persistence.PersonDAO;
import payup.server.ServiceRegistry;

@DisplayName("For the expenses page")
public class ExpensesPageTests extends WebTestRunner {
    private final WebSession session = new WebSession(ExpensesPageTests.this);

    @Override
    protected void setupTestData() {
        PersonDAO personDAO = ServiceRegistry.lookup(PersonDAO.class);
        ExpenseDAO expenseDAO = ServiceRegistry.lookup(ExpenseDAO.class);

        Person firstTimeUser = new Person("firsttimeuser@mockemail.com");
        Person p1 = new Person("student1@mockemail.com");
        Person p2 = new Person("student2@mockemail.com");
        Person p3 = new Person("student3@mockemail.com");
        Stream.of(firstTimeUser, p1, p2).forEach(personDAO::savePerson);

        Expense expense1 = new Expense(p1, "Lunch", amountOf(300), TODAY);
        Expense expense2 = new Expense(p1, "Airtime", amountOf(100), TODAY);
        Expense expense3 = new Expense(p2, "Movies", amountOf(100), TODAY.minusDays(1));
        PaymentRequest paymentRequest1 = expense3.requestPayment(p3, amountOf(100), TOMORROW);
        expense3.payPaymentRequest(paymentRequest1.getId(), p3, TODAY);
        Stream.of(expense1, expense2, expense3).forEach(expenseDAO::save);
    }

    @Test
    @DisplayName("when I have no expenses")
    void noExpenses() throws IOException {
        session.openLoginPage()
                .login("firsttimeuser@mockemail.com")
                .shouldBeOnExpensesPage()
                .shouldHaveNoExpenses()
                .takeScreenshot("home");
    }

    @Test
    @DisplayName("when I have some expenses")
    public void haveExpenses() throws IOException {
        session.openLoginPage()
                .login("student1@mockemail.com")
                .shouldBeOnExpensesPage()
                .shouldHaveExpense("Lunch")
                .shouldHaveExpense("Airtime")
                .expensesGrandTotalShouldBe(amountOf(400))
                .takeScreenshot("home");
    }

    @Test
    @DisplayName("when my expense is paid in full")
    public void expensePaidInFull() throws IOException {
        session.openLoginPage()
                .login("student2@mockemail.com")
                .shouldBeOnExpensesPage()
                .shouldHaveNoExpenses()
                .takeScreenshot("home");
    }
}
