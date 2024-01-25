package redis.clone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RedisClone {

    private static final Logger logger = LogManager.getLogger(RedisClone.class);

    public static void main(String[] args) {
        int port = 6379; // default Redis port
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                logger.error("Invalid port number. Using default port 6379.");
            }
        }

        final RedisServer server = new RedisServer(port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down the server...");
            server.stopServer();
        }));

        logger.info("Starting the server on port : {}", port);
        server.startServer();
    }
}

