package payup.model;

import static org.assertj.core.api.Assertions.*;

/*
 ** DO NOT CHANGE!!
 */

import org.junit.jupiter.api.Test;

public class PersonTests {
    @Test
    public void invalidEmailAddressFails() {
        assertThatThrownBy(() -> new Person("not an email"))
                .isInstanceOf(PayUpException.class)
                .hasMessageContaining("Bad email address");
    }

    @Test
    public void nameFromEmailAddress() {
        Person p = new Person("student@mockemail.com");
        assertThat(p.getName()).isEqualTo("Student");
    }
}
