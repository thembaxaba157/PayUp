package payup.controller;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.function.MonetaryFunctions;

import io.javalin.http.Handler;
import payup.model.Expense;
import payup.model.Person;
import payup.persistence.ExpenseDAO;
import payup.server.Routes;
import payup.server.ServiceRegistry;
import payup.server.PayUpServer;

import static payup.model.MoneyHelper.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
public class NewExpenseController {


      public static final Handler view = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = PayUpServer.getPersonLoggedIn(context);

        Collection<Expense> expenses = expensesDAO.findExpensesForPerson(personLoggedIn);
        MonetaryAmount totalExpense = expenses.stream()
        .map(Expense::amountLessPaymentsReceived)
        .reduce(ZERO_RANDS, MonetaryFunctions.sum());
        Map<String, Object> viewModel = Map.of("expenses", expenses, "totalExpense",totalExpense);
        context.render("newexpense.html", viewModel);
    };
      public static final Handler newexpense = context -> {
          ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
          Person personLoggedIn = PayUpServer.getPersonLoggedIn(context);
          String description = context.formParam("description");
          LocalDate date =  convertToLocalDate(context.formParam("date"));
          String amount =  context.formParam("amount");
          Expense expense2 = new Expense(personLoggedIn, description, amountOf(Integer.parseInt(amount)), date);
          expensesDAO.save(expense2);
        context.redirect(Routes.EXPENSES);
    };
    
    
    private static LocalDate convertToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateString, formatter);
      }

}
