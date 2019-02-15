package ia.org.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClientConnectionTest {

    @Test
    public void isStatic() {

        assertTrue(ClientConnection.isStaticFile("/pdf.pdf"));

    }

    @Test
    public void isPlugin() {
        assertTrue(ClientConnection.isPlugin("Greetings/"));
    }
}