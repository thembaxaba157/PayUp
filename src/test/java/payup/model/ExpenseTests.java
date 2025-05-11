package payup.model;

import static org.assertj.core.api.Assertions.*;
import static payup.model.DateHelper.*;
import static payup.model.MoneyHelper.*;

import javax.money.MonetaryAmount;

/*
 ** DO NOT CHANGE!!
 */

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

public class ExpenseTests {
    private final Person p = new Person("student@mockemail.com");
    private final MonetaryAmount R100 = Money.of(100, "ZAR");

    @Test
    public void newExpense() {
        Expense e = new Expense(p, "Lunch", R100, TODAY);
        assertThat(e.getId()).isNotNull();
        assertThat(p).isEqualTo(e.getPerson());
        assertThat(e.totalAmountOfPaymentsRequested()).isEqualTo(ZERO_RANDS);
        assertThat(e.listOfPaymentRequests()).isEmpty();
    }

    @Test
    void emptyDescriptionDefaultsToUnspecified() {
        Expense e = new Expense(p, "", R100, TODAY);
        assertThat(e.getDescription()).isEqualTo("Unspecified");
    }

    @Test
    void nullDescriptionDefaultsToUnspecified() {
        Expense e = new Expense(p, null, R100, TODAY);
        assertThat(e.getDescription()).isEqualTo("Unspecified");
    }

    @Test
    void expenseCannotBeInTheFuture() {
        assertThatThrownBy(() -> new Expense(p, "Airtime", R100, TODAY.plusDays(1)))
                .isInstanceOf(PayUpException.class)
                .hasMessageContaining("Expense cannot be in the future");
    }
}
