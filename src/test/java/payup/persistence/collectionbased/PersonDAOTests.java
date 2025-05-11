package payup.persistence.collectionbased;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import java.util.Set;

/*
 ** DO NOT CHANGE!!
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import payup.model.Person;
import payup.persistence.PersonDAO;

public class PersonDAOTests {
    private PersonDAO dao;

    @BeforeEach
    public void newRepository() {
        Set<Person> people = Set.of(new Person("student1@mockemail.com"));
        this.dao = new PersonDAOImpl(people);
    }

    @Test
    public void findPerson() {
        Optional<Person> retrievedPerson = dao.findPersonByEmail("student1@mockemail.com");
        assertThat(retrievedPerson.isPresent()).isTrue();
    }

    @Test
    public void savePerson() {
        Person p = new Person("student2@mockemail.com");
        Person savedPerson = dao.savePerson(p);
        Optional<Person> retrievedPerson = dao.findPersonByEmail(savedPerson.getEmail());
        assertThat(retrievedPerson.isPresent()).isTrue();
    }

    @Test
    public void personNotFound() {
        Optional<Person> retrievedPerson = dao.findPersonByEmail("student@mockemail.com");
        assertThat(retrievedPerson.isEmpty()).isTrue();
    }
}
