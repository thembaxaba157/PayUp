package payup.server;

import static io.javalin.apibuilder.ApiBuilder.*;

import payup.controller.ExpensesController;
import payup.controller.NewExpenseController;
import payup.controller.PayReqReceivedController;
import payup.controller.PayReqSentController;
import payup.controller.PaymentRequestController;
import payup.controller.PersonController;

public class Routes {
    public static final String LOGIN_PAGE = "/";
    public static final String LOGIN_ACTION = "/login.action";
    public static final String LOGOUT = "/logout";
    public static final String EXPENSES = "/expenses";
    public static final String PAYMENTREQUESTS_SENT = "/paymentrequests_sent";
    public static final String PAYMENTREQUESTS_RECEIVED = "/paymentrequests_received";
    public static final String NEW_EXPENSE = "/newexpense";
    public static final String EXPENSE_ACTION = "/expense.action";
    public static final String PAYMENT_ACTION = "/payment.action";
    public static final String PAYMENT_REQUEST_ACTION = "/paymentrequest.action";
    public static final String PAYMENT_REQUEST = "/paymentrequest";

    public static void configure(PayUpServer server) {
        server.routes(() -> {
            post(LOGIN_ACTION,  PersonController.login);
            get(LOGOUT,         PersonController.logout);
            get(EXPENSES,           ExpensesController.view);
            get(PAYMENTREQUESTS_SENT,   PayReqSentController.view);
            get(PAYMENTREQUESTS_RECEIVED, PayReqReceivedController.view);
            post(PAYMENT_ACTION, PayReqReceivedController.payment);
            get(NEW_EXPENSE, NewExpenseController.view);
            post(EXPENSE_ACTION, NewExpenseController.newexpense);
            get(PAYMENT_REQUEST, PaymentRequestController.view);
            post(PAYMENT_REQUEST_ACTION, PaymentRequestController.request);
        });
    }
}
