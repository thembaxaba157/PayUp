package payup.model;

import static org.assertj.core.api.Assertions.*;
import static payup.model.DateHelper.*;
import static payup.model.MoneyHelper.*;

import java.time.LocalDate;
import java.util.List;

import javax.money.MonetaryAmount;

/*
** DO NOT CHANGE!!
 */

import org.junit.jupiter.api.Test;

public class PaymentRequestTests {

    private final LocalDate IN_FIVE_DAYS = TODAY.plusDays(5);
    private final MonetaryAmount R100 = amountOf(100);
    private final MonetaryAmount R120 = amountOf(120);
    private final MonetaryAmount R300 = amountOf(300);
    private final Person personWhoPaidForSomeone = new Person("ipaidforyou@mockemail.com");
    private final Person personWhoShouldPayBack = new Person("ioweyou@mockemail.com");

    @Test
    public void requestFullPaymentFromOnePerson() {
        // The personWhoPaidForSomeone paid for Airtime
        Expense airtime = new Expense(personWhoPaidForSomeone, "Airtime", R100, TODAY);

        // Create a payment request for the airtime from the personWhoShouldPayBack
        PaymentRequest paymentRequest = airtime.requestPayment(personWhoShouldPayBack, R100, IN_FIVE_DAYS);

        // The payment request was added to the expense
        assertThat(List.of(paymentRequest)).hasSameElementsAs(airtime.listOfPaymentRequests());

        // The payment request has the correct expense
        assertThat(airtime.getId()).isEqualTo(paymentRequest.getExpenseId());

        // The person requesting the payment is the person that incurred the expense
        assertThat(airtime.getPerson()).isEqualTo(paymentRequest.getPersonRequestingPayment());

        // The description of the payment request is the same as that of the expense
        assertThat(airtime.getDescription()).isEqualTo(paymentRequest.getDescription());

        // The total of payment requests is the full amount of the expense
        assertThat(airtime.getAmount()).isEqualTo(airtime.totalAmountOfPaymentsRequested());

        // The number of days left to pay the payment request, catering for a 1 day error for midnight tests
        assertThat(paymentRequest.daysLeftToPay()).isLessThanOrEqualTo(5);

        // The total available for payment requests
        assertThat(airtime.totalAmountAvailableForPaymentRequests())
                .isEqualTo(airtime.getAmount().subtract(airtime.totalAmountOfPaymentsRequested()));
    }

    @Test
    public void cannotRequestPaymentFromYourself() {
        Expense airtime = new Expense(personWhoPaidForSomeone, "Airtime", R100, TODAY);
        assertThatThrownBy(() -> airtime.requestPayment(personWhoPaidForSomeone, R100, IN_FIVE_DAYS))
                .isInstanceOf(PayUpException.class)
                .hasMessageContaining("You cannot request payment from yourself");
    }

    @Test
    public void cannotRequestPaymentAmountGreaterThanExpenseAmount() {
        Expense airtime = new Expense(personWhoPaidForSomeone, "Airtime", R100, TODAY);
        assertThatThrownBy(() -> airtime.requestPayment(personWhoShouldPayBack, R120, IN_FIVE_DAYS))
                .isInstanceOf(PayUpException.class)
                .hasMessageContaining("Total requested amount is more than the expense amount");
    }

    @Test
    public void cannotRequestPaymentBeforeExpenseDate() {
        Expense airtime = new Expense(personWhoPaidForSomeone, "Airtime", R100, TODAY);
        assertThatThrownBy(() -> airtime.requestPayment(personWhoShouldPayBack, R100, airtime.getDate().minusDays(2)))
                .isInstanceOf(PayUpException.class)
                .hasMessageContaining("Payment request cannot be due before the expense was incurred");
    }

    @Test
    public void moreThanOnePaymentRequestForSameExpense() {
        Person anotherPersonThatShouldPay = new Person("another@mockemail.com");
        Expense lunch = new Expense(personWhoPaidForSomeone, "Lunch", R300, TODAY);

        // make two payment requests
        PaymentRequest paymentRequest1 = lunch.requestPayment(personWhoShouldPayBack, R100, TOMORROW);
        PaymentRequest paymentRequest2 = lunch.requestPayment(anotherPersonThatShouldPay, R100, TOMORROW);

        // expense should have both payment requests
        assertThat(List.of(paymentRequest1, paymentRequest2)).hasSameElementsAs(lunch.listOfPaymentRequests());

        // The total of payment requests is the full amount of the expense
        assertThat(lunch.totalAmountOfPaymentsRequested()).isEqualTo(paymentRequest1.getAmountToPay().add(paymentRequest2.getAmountToPay()));
    }

    @Test
    public void totalOfMoreThanOnePaymentRequestExceedsExpenseAmount() {
        Person anotherPersonThatShouldPay = new Person("another@mockemail.com");
        Expense lunch = new Expense(personWhoPaidForSomeone, "Lunch", R300, TODAY);

        PaymentRequest paymentRequest1 = lunch.requestPayment(personWhoShouldPayBack, R100, TOMORROW);
        assertThatThrownBy(() -> lunch.requestPayment(anotherPersonThatShouldPay, R300, TOMORROW))
                .isInstanceOf(PayUpException.class)
                .hasMessageContaining("Total requested amount is more than the expense amount");
    }
}
