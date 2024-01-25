package redis.clone.protocol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SimpleRedisProtocolHandler {

    public static final byte DOLLAR_BYTE = '$';
    public static final byte ASTERISK_BYTE = '*';
    public static final byte PLUS_BYTE = '+';
    public static final byte MINUS_BYTE = '-';
    public static final byte COLON_BYTE = ':';
    private static final Logger logger = LogManager.getLogger(SimpleRedisProtocolHandler.class);

    public static Object parseRequest(ReadableByteChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1);
        read(channel, buffer);
        buffer.flip();

        byte b = buffer.get();
        switch (b) {
            case PLUS_BYTE:
                return processSimpleString(channel);
            case MINUS_BYTE:
                return processError(channel);
            case COLON_BYTE:
                return processInteger(channel);
            case DOLLAR_BYTE:
                return processBulkString(channel);
            case ASTERISK_BYTE:
                return processArray(channel);
            default:
                throw new IOException("Unknown RESP type: " + (char) b);
        }
    }

    private static String processSimpleString(ReadableByteChannel channel) throws IOException {
        return readLine(channel);
    }

    private static String processError(ReadableByteChannel channel) throws IOException {
        return readLine(channel);
    }

    private static Long processInteger(ReadableByteChannel channel) throws IOException {
        return Long.parseLong(readLine(channel));
    }

    private static String processBulkString(ReadableByteChannel channel) throws IOException {
        int len = Integer.parseInt(readLine(channel));
        if (len == -1) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.allocate(len);
        read(channel, buffer);
        buffer.flip();

        byte[] bytes = new byte[len];
        buffer.get(bytes);

        ByteBuffer crlfBuffer = ByteBuffer.allocate(2);
        read(channel, crlfBuffer);

        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static List<Object> processArray(ReadableByteChannel channel) throws IOException {
        int numElements = Integer.parseInt(readLine(channel));
        if (numElements == -1) {
            return null;
        }
        List<Object> array = new ArrayList<>(numElements);
        for (int i = 0; i < numElements; i++) {
            array.add(parseRequest(channel));
        }
        return array;
    }

    private static void read(ReadableByteChannel channel, ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            if (channel.read(buffer) == -1) {
                throw new IOException("Unexpected end of stream");
            }
        }
    }

    private static String readLine(ReadableByteChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1); // Allocate once
        StringBuilder sb = new StringBuilder();
        while (true) {
            buffer.clear(); // Prepare the buffer for reading
            int bytesRead = channel.read(buffer);
            if (bytesRead == -1) {
                // End of stream, the client has disconnected
                if (sb.length() == 0) {
                    return null; // No data read, return null to signify end of stream
                }
                break; // If we have partial data, return it as a line
            }
            if (bytesRead > 0) {
                buffer.flip(); // Prepare the buffer for reading
                char c = (char) buffer.get();
                if (c == '\r') {
                    // Expecting \n after \r
                    buffer.clear();
                    if (channel.read(buffer) != 1 || buffer.get(0) != '\n') {
                        throw new IOException("Invalid line ending found");
                    }
                    break; // Line is complete
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}