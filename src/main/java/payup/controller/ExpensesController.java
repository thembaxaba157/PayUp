package payup.controller;

import io.javalin.http.Handler;
import payup.model.Expense;
import payup.model.Person;
import payup.persistence.ExpenseDAO;
import payup.server.ServiceRegistry;
import payup.server.PayUpServer;

import org.javamoney.moneta.function.MonetaryFunctions;

import javax.money.MonetaryAmount;

import static payup.model.MoneyHelper.*;

import java.util.Collection;
import java.util.Map;

public class ExpensesController {

    public static final Handler view = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = PayUpServer.getPersonLoggedIn(context);

        Collection<Expense> expenses = expensesDAO.findExpensesForPerson(personLoggedIn);
        MonetaryAmount totalExpense = expenses.stream()
        .map(Expense::amountLessPaymentsReceived)
        .reduce(ZERO_RANDS, MonetaryFunctions.sum());
         Map<String, Object> viewModel = Map.of("expenses", expenses, "totalExpense",totalExpense.getNumber().intValue(), "grandExpense",totalExpense);
        
        context.render("expenses.html", viewModel);
    };
}
