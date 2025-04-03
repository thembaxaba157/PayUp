package weshare.controller;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.function.MonetaryFunctions;
import static weshare.model.MoneyHelper.ZERO_RANDS;

import io.javalin.http.Handler;
import weshare.model.Payment;
import weshare.model.PaymentRequest;
import weshare.model.Person;
import weshare.persistence.ExpenseDAO;

import weshare.server.Routes;
import weshare.server.ServiceRegistry;
import weshare.server.WeShareServer;
public class PayReqReceivedController {
    

  
    public static final Handler view = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);
        Collection<PaymentRequest> paymentReceived = expensesDAO.findPaymentRequestsReceived(personLoggedIn);
         MonetaryAmount totalpaymentReceived = paymentReceived.stream()
        .map(PaymentRequest::getAmountToPay)
        .reduce(ZERO_RANDS, MonetaryFunctions.sum());
        Map<String, Object> viewModel = Map.of("paymentRequestsReceived", paymentReceived,"totalPaid", totalpaymentReceived);
        context.render("paymentRequestsReceived.html", viewModel);
    };
    public static final Handler payment = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);
        Collection<PaymentRequest> paymentReceived = expensesDAO.findPaymentRequestsReceived(personLoggedIn);
        String paymentRequestId = context.formParam("paymentRequestId");

        PaymentRequest paidPaymentRequest = findPaymentRequest(paymentRequestId, paymentReceived);
        
        Payment newPayment = paidPaymentRequest.pay(personLoggedIn, LocalDate.now());
        expensesDAO.save(newPayment.getExpenseForPersonPaying());

        context.redirect(Routes.PAYMENTREQUESTS_RECEIVED);
    };


    private static PaymentRequest findPaymentRequest(String targetPaymentRequestId, Collection<PaymentRequest> paymentReceived){
        Optional<PaymentRequest> foundPaymentRequest = paymentReceived.stream()
        .filter(paymentRequest -> paymentRequest.getId().toString().equals(targetPaymentRequestId))
        .findFirst();
           return foundPaymentRequest.get();
    }
       
    
}
