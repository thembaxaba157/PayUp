package payup.server;

/*
 ** DO NOT CHANGE!!
 */

import org.junit.jupiter.api.Test;

import payup.persistence.PersonDAO;
import payup.persistence.collectionbased.PersonDAOImpl;
import payup.server.ServiceRegistry;
import payup.server.PayUpServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ServiceRegistryTests {

    @Test
    public void configureService() {
        PersonDAO dao = new PersonDAOImpl();
        ServiceRegistry.configure(PersonDAO.class, dao);
        assertThat(ServiceRegistry.lookup(PersonDAO.class)).isEqualTo(dao);
    }

    @Test
    public void registryNotInitialised() {
        assertThatThrownBy(() -> ServiceRegistry.lookup(PayUpServer.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessageStartingWith("No service configured for");
    }
}
