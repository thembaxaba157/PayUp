package weshare.controller;

import java.util.Collection;
import java.util.Map;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.function.MonetaryFunctions;
import static weshare.model.MoneyHelper.ZERO_RANDS;

import io.javalin.http.Handler;
import weshare.model.PaymentRequest;
import weshare.model.Person;
import weshare.persistence.ExpenseDAO;
import weshare.server.ServiceRegistry;
import weshare.server.WeShareServer;

public class PayReqSentController {
     public static final Handler view = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);
        Collection<PaymentRequest> paymentRequests = expensesDAO.findPaymentRequestsSent(personLoggedIn);
         MonetaryAmount totalpaymentRequests = paymentRequests.stream()
        .map(PaymentRequest::getAmountToPay)
        .reduce(ZERO_RANDS, MonetaryFunctions.sum());
        Map<String, Object> viewModel = Map.of("paymentRequestsSent", paymentRequests,"totalUnpaid", totalpaymentRequests);
        context.render("paymentRequestsSent.html", viewModel);
    };
}
