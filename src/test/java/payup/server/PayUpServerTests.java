package payup.server;

/*
 ** DO NOT CHANGE!!
 */

import org.junit.jupiter.api.Test;

import payup.server.PayUpServer;

import static org.assertj.core.api.Assertions.assertThat;

public class PayUpServerTests {

    @Test
    public void ifServerStartsThenItIsProperlyConfigured() {
        PayUpServer server = new PayUpServer();
        server.start(0);
        assertThat(server.port()).isGreaterThan(0);
    }
}
