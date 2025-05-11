package payup.persistence;

import java.util.Optional;

import payup.model.Person;

public interface PersonDAO {
    Optional<Person> findPersonByEmail(String email);
    Person savePerson(Person person);
}
