package weshare.controller;
import weshare.model.Expense;
import weshare.model.Person;
import weshare.persistence.ExpenseDAO;
import weshare.server.Routes;
import weshare.server.ServiceRegistry;
import weshare.server.WeShareServer;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.function.MonetaryFunctions;

import io.javalin.http.Handler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;


import static weshare.model.MoneyHelper.ZERO_RANDS;
import static weshare.model.MoneyHelper.amountOf;
public class NewExpenseController {


      public static final Handler view = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);

        Collection<Expense> expenses = expensesDAO.findExpensesForPerson(personLoggedIn);
        MonetaryAmount totalExpense = expenses.stream()
        .map(Expense::amountLessPaymentsReceived)
        .reduce(ZERO_RANDS, MonetaryFunctions.sum());
        Map<String, Object> viewModel = Map.of("expenses", expenses, "totalExpense",totalExpense);
        context.render("newexpense.html", viewModel);
    };
      public static final Handler newexpense = context -> {
          ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
          Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);
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
