package payup.persistence.collectionbased;

import static org.assertj.core.api.Assertions.*;
import static payup.model.DateHelper.*;
import static payup.model.MoneyHelper.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 ** DO NOT CHANGE!!
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import payup.model.Expense;
import payup.model.PaymentRequest;
import payup.model.Person;
import payup.persistence.ExpenseDAO;

public class ExpenseDAOTests {

    private ExpenseDAO dao;
    private List<PaymentRequest> paymentRequestsSentByStudent1;
    private List<PaymentRequest> paymentRequestsReceivedByStudent2;

    @BeforeEach
    public void newRepository() {
        Person student1 = new Person("student1@mockemail.com");
        Person student2 = new Person("student2@mockemail.com");
        Person student3 = new Person("student3@mockemail.com");

        Expense expense1 = new Expense(student1, "Lunch", amountOf(300), TODAY);
        Expense expense2 = new Expense(student1, "Airtime", amountOf(100), TODAY);
        Expense expense3 = new Expense(student2, "Movies", amountOf(150), TODAY.minusWeeks(1));
        Expense expense4 = new Expense(student3, "Ice cream", amountOf(50), TODAY.minusDays(3));

        PaymentRequest paymentRequest1 = expense1.requestPayment(student2, amountOf(100), TOMORROW);
        PaymentRequest paymentRequest2 = expense1.requestPayment(student3, amountOf(100), TOMORROW);
        PaymentRequest paymentRequest3 = expense2.requestPayment(student2, amountOf(100), TOMORROW);
        paymentRequestsSentByStudent1 = List.of(paymentRequest1, paymentRequest2, paymentRequest3);
        paymentRequestsReceivedByStudent2 = List.of(paymentRequest1, paymentRequest3);

        Map<UUID, Expense> expenses = Stream.of(expense1, expense2, expense3, expense4)
                .collect(Collectors.toConcurrentMap(Expense::getId, e -> e));
        this.dao = new ExpenseDAOImpl(expenses);
    }

    @Test
    public void findExpensesForPerson() {
        Person p = new Person("student1@mockemail.com");
        Collection<Expense> expenses = dao.findExpensesForPerson(p);
        assertThat(expenses).isNotEmpty();
        List<String> descriptions = List.of("Lunch", "Airtime");
        assertThat(descriptions).hasSameElementsAs(expenses.stream().map(Expense::getDescription).collect(Collectors.toList()));
    }

    @Test
    public void noExpensesForPerson() {
        Person p = new Person("unknownstudent@mockemail.com");
        Collection<Expense> expenses = dao.findExpensesForPerson(p);
        assertThat(expenses).isEmpty();
    }

    @Test
    public void saveExpense() {
        Person p = new Person("newstudent@mockemail.com");
        Expense e = new Expense(p, "Some expense", amountOf(100), TODAY);
        Expense saved = dao.save(e);
        assertThat(saved).isEqualTo(e);
        assertThat(saved).isEqualTo(dao.get(e.getId()).orElseThrow());
    }

    @Test
    public void getExpense() {
        Person p = new Person("student1@mockemail.com");
        Expense expenseToFind = dao.findExpensesForPerson(p).stream().findFirst().orElseThrow();
        Expense foundExpense = dao.get(expenseToFind.getId()).orElseThrow();
        assertThat(foundExpense).isEqualTo(expenseToFind);
    }

    @Test
    public void paymentRequestSentForPerson() {
        Person p = new Person("student1@mockemail.com");
        Collection<PaymentRequest> paymentRequestsForPerson = dao.findPaymentRequestsSent(p);
        assertThat(paymentRequestsForPerson).hasSameElementsAs(paymentRequestsSentByStudent1);
    }

    @Test
    public void paymentRequestReceivedForPerson() {
        Person p = new Person("student2@mockemail.com");
        Collection<PaymentRequest> paymentRequestsForPerson = dao.findPaymentRequestsReceived(p);
        assertThat(paymentRequestsForPerson).hasSameElementsAs(paymentRequestsReceivedByStudent2);
    }
}
