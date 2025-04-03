package weshare.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.function.MonetaryFunctions;

import io.javalin.http.Handler;

import weshare.model.Expense;
import weshare.model.PaymentRequest;
import weshare.model.Person;
import weshare.persistence.ExpenseDAO;
import weshare.persistence.PersonDAO;
import weshare.server.Routes;
import weshare.server.ServiceRegistry;
import weshare.server.WeShareServer;

import static weshare.model.MoneyHelper.ZERO_RANDS;
import static weshare.model.MoneyHelper.amountOf;
public class PaymentRequestController {

      public static final Handler view = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);
        String expenseId = context.queryParam("expenseId");
        
        // Fetch the specific expense using the expenseId from your data source
        ExpenseDAO expenseDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Expense expense = expenseDAO.get(UUID.fromString(expenseId)).get(); // Implement getExpenseById() method
        Collection<PaymentRequest> paymentRequests = expense.listOfPaymentRequests();
        MonetaryAmount totalpaymentRequests = paymentRequests.stream()
        .map(PaymentRequest::getAmountToPay)
        .reduce(ZERO_RANDS, MonetaryFunctions.sum());
        // Process the payment request logic...
        // For example, you can render a payment request page with the details of the selected expense.
        
        // You can pass the expense object to the payment request page in the viewModel
        
        int nettExpense = expense.getAmount().getNumber().intValue()-totalpaymentRequests.getNumber().intValue();
        MonetaryAmount m = amountOf(nettExpense);
        context.render("paymentRequests.html", Map.of("expense", expense,
        "paymentRequests",paymentRequests,
        "grandTotal",totalpaymentRequests,
        "maximumAmount", nettExpense,
        "maximumMoney", m));
    };
    private static final PersonDAO personDAO = ServiceRegistry.lookup(PersonDAO.class);
    public static final Handler request = context -> {
      
      ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
      Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);
      String expenseId = context.formParam("expenseId");
        
      // Fetch the specific expense using the expenseId from your data source
      ExpenseDAO expenseDAO = ServiceRegistry.lookup(ExpenseDAO.class);
      Expense expense = expenseDAO.get(UUID.fromString(expenseId)).get();
      String email = context.formParam("email");
      Person person = personDAO.findPersonByEmail(email).get();
      LocalDate date =  convertToLocalDate(context.formParam("due_date"));
      int amount =  Integer.parseInt(context.formParam("amount"));

      expense.requestPayment(person, amountOf(amount), date);

      context.redirect(Routes.PAYMENT_REQUEST+"?expenseId="+expenseId);
};
     

private static LocalDate convertToLocalDate(String dateString) {
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  return LocalDate.parse(dateString, formatter);
}
}
