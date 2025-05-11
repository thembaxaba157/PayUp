package payup.controller;

import static payup.model.MoneyHelper.*;

import java.util.Collection;
import java.util.Map;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.function.MonetaryFunctions;

import io.javalin.http.Handler;
import payup.model.PaymentRequest;
import payup.model.Person;
import payup.persistence.ExpenseDAO;
import payup.server.ServiceRegistry;
import payup.server.PayUpServer;

public class PayReqSentController {
     public static final Handler view = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = PayUpServer.getPersonLoggedIn(context);
        Collection<PaymentRequest> paymentRequests = expensesDAO.findPaymentRequestsSent(personLoggedIn);
         MonetaryAmount totalpaymentRequests = paymentRequests.stream()
        .map(PaymentRequest::getAmountToPay)
        .reduce(ZERO_RANDS, MonetaryFunctions.sum());
        Map<String, Object> viewModel = Map.of("paymentRequestsSent", paymentRequests,"totalUnpaid", totalpaymentRequests);
        context.render("paymentRequestsSent.html", viewModel);
    };
}
