package redis.clone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clone.command.CommandHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisServer {

    private static final Logger logger = LogManager.getLogger(RedisClone.class);
    private final int port;
    private final ExecutorService executorService;
    private ServerSocket serverSocket;
    private volatile boolean running = true;

    public RedisServer(int port) {
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    executorService.submit(new ClientHandler(clientSocket, new CommandHandler(KeyValueStore.getInstance())));
                } catch (IOException e) {
                    if (!running) break;
                    throw new RuntimeException("Error accepting client connection", e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not listen on port " + port, e);
        }
    }

    public void stopServer() {
        running = false;
        executorService.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            logger.error("Error While Shutting Down the Redis Clone Server {} ", e.getMessage());
        }
    }

}
