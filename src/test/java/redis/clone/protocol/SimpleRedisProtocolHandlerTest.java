package redis.clone.protocol;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import redis.clone.protocol.SimpleRedisProtocolHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpleRedisProtocolHandlerTest {

    static class RequestArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("+OK\r\n", "OK"),
                    Arguments.of("-Error message\r\n", "Error message"),
                    Arguments.of(":1000\r\n", 1000L),
                    Arguments.of(":1000\r\n", 1000L),
                    Arguments.of("$6\r\nfoobar\r\n", "foobar"),
                    Arguments.of("*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n", List.of("foo", "bar")),
                    Arguments.of("*-1\r\n", null),
                    Arguments.of("$-1\r\n", null),
                    Arguments.of("*1\r\n$4\r\nPING\r\n",List.of("PING"))
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(RequestArgumentsProvider.class)
    void testParseRequest(String input, Object expected) throws IOException {
        ReadableByteChannel mockChannel = Channels.newChannel(new ByteArrayInputStream(input.getBytes()));

        Object result = SimpleRedisProtocolHandler.parseRequest(mockChannel);

        if (result instanceof List) {
            assertTrue(((List<?>) result).equals(expected));
        } else {
            assertEquals(expected, result);
        }
    }
}
