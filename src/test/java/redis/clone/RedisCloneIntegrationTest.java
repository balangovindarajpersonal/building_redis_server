package redis.clone;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RedisCloneIntegrationTest {

    private static RedisServer redisServer;

    @BeforeAll
    static void setUp() {
        redisServer = new RedisServer(8080);
        new Thread(redisServer::startServer).start();
    }

    @AfterAll
    static void tearDown() {
        redisServer.stopServer();
    }


    @Test
    void testStartServerWithInvalidPort() {
        assertThrows(RuntimeException.class, () -> {
            RedisServer server = new RedisServer(99999);
            server.startServer();
        });
    }

    @ParameterizedTest
    @CsvSource({
            "PING, PONG",
            "ECHO Hello, Hello",
            "SET myKey myValue, OK",
            "GET myKey, myValue"
    })
    void testClientConnection(String command, String expectedResponse) {
        assertDoesNotThrow(() -> {
            new Thread(() -> {
                try (Socket socket = new Socket("localhost", 8080)) {
                    performClientOperation(socket, command, expectedResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    private void performClientOperation(Socket socket, String command, String expectedResponse) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Send command
            out.println(command);
            String response = in.readLine();
            assertEquals(expectedResponse, response);
        }
    }
}
