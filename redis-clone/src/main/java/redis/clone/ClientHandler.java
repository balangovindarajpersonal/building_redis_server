package redis.clone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clone.command.CommandHandler;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ClientHandler implements Runnable {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);
    private final Socket clientSocket;
    private final CommandHandler commandHandler;

    public ClientHandler(Socket socket, CommandHandler commandHandler) {
        this.clientSocket = socket;
        this.commandHandler = commandHandler;
    }

    public void run() {

        try {
            while (clientSocket.isConnected() && !clientSocket.isClosed()) {
                Object request = SimpleRedisProtocolHandler.parseRequest(Channels.newChannel(clientSocket.getInputStream()));
                if (request instanceof List) {
                    List<String> commandList = (List<String>) request;
                    if (!commandList.isEmpty()) {
                        String response = processRequest(commandList);
                        sendResponse(response);
                        if ("QUIT".equalsIgnoreCase(String.valueOf(request))) {
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error processing request: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error processing request: {}", e.getStackTrace());
            throw e;
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.error("Error closing client socket: {}", e.getMessage());
            }
        }
    }

    private void sendResponse(String response) throws IOException {
        try {
            ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8));
            clientSocket.getOutputStream().write(responseBuffer.array());
        } catch (IOException e) {
            logger.error("Error sending response: {}", e.getMessage());
        }
    }

    private String processRequest(List<String> commandList) {

        String[] commands = commandList.toArray(new String[0]);
        return commandHandler.handleCommand(commands[0], Arrays.copyOfRange(commands, 1, commands.length));
    }
}
