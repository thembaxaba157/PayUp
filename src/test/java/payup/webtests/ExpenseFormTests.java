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

@DisplayName("For expenses")
public class ExpenseFormTests extends WebTestRunner {

    private final WebSession session = new WebSession(ExpenseFormTests.this);

    @Override
    protected void setupTestData() {
        PersonDAO personDAO = ServiceRegistry.lookup(PersonDAO.class);
        ExpenseDAO expenseDAO = ServiceRegistry.lookup(ExpenseDAO.class);

        Person p = new Person("student1@mockemail.com");
        personDAO.savePerson(p);

        Stream.of(new Expense(p, "Lunch", amountOf(300), TODAY),
                        new Expense(p, "Airtime", amountOf(100), TODAY))
                .forEach(expenseDAO::save);
    }

    @Test
    @DisplayName("I can capture a new expense")
    public void captureExpense() throws IOException {
        session.openLoginPage()
                .login("student1@mockemail.com")
                .shouldBeOnExpensesPage()
                .clickOnCaptureExpense()
                .shouldBeOnCaptureExpensePage()
                .takeScreenshot("new-expense")
                .fillExpenseForm("Movies", amountOf(200), TODAY)
                .takeScreenshot("capture-expense")
                .submitExpenseForm()
                .shouldBeOnExpensesPage()
                .shouldHaveExpense("Movies")
                .expensesGrandTotalShouldBe(amountOf(600))
                .takeScreenshot("home");
    }
}
