package payup.persistence;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import payup.model.Expense;
import payup.model.PaymentRequest;
import payup.model.Person;

public interface ExpenseDAO {
    Collection<Expense> findExpensesForPerson(Person person);

    Expense save(Expense expense);

    Optional<Expense> get(UUID id);

    Collection<PaymentRequest> findPaymentRequestsSent(Person person);

    Collection<PaymentRequest> findPaymentRequestsReceived(Person person);
}
