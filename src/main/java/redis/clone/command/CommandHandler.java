package redis.clone.command;

import redis.clone.KeyValueStore;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private final Map<String, CommandStrategy> commandMap;
    private final KeyValueStore keyValueStore;

    public CommandHandler(KeyValueStore keyValueStore) {
        this.keyValueStore = keyValueStore;
        commandMap = new HashMap<>();
        commandMap.put("PING", new PingCommandStrategy());
        commandMap.put("ECHO", new EchoCommandStrategy());
        commandMap.put("SET", new SetCommandStrategy(this.keyValueStore));
        commandMap.put("GET", new GetCommandStrategy(this.keyValueStore));
    }

    public String handleCommand(String requestCommand, String[] request) {
        if (requestCommand == null) {
            return "-ERR Client disconnected\r\n";
        }

        String[] parts = requestCommand.trim().split("\\s+");
        if (parts.length == 0) {
            return "-ERR Empty request\r\n";
        }

        String command = parts[0].toUpperCase();
        CommandStrategy strategy = commandMap.get(command);

        if (strategy != null) {
            return strategy.execute(request);
        } else {
            return "-ERR Unknown command\r\n";
        }
    }
}
