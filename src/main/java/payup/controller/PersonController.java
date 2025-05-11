package payup.controller;

import java.util.Objects;

import io.javalin.http.Handler;
import payup.model.Person;
import payup.persistence.PersonDAO;
import payup.server.PayUpServer;
import payup.server.Routes;
import payup.server.ServiceRegistry;

public class PersonController {

    public static final Handler logout = ctx -> {
        ctx.sessionAttribute(PayUpServer.SESSION_USER_KEY, null);
        ctx.redirect(Routes.LOGIN_PAGE);
    };

    private static final PersonDAO personDAO = ServiceRegistry.lookup(PersonDAO.class);
    public static final Handler login = context -> {
        String email = context.formParamAsClass("email", String.class)
                .check(Objects::nonNull, "Email is required")
                .get();

        Person person = personDAO.savePerson(new Person(email));
        context.sessionAttribute(PayUpServer.SESSION_USER_KEY, person);
        context.redirect(Routes.EXPENSES);
    };
}
