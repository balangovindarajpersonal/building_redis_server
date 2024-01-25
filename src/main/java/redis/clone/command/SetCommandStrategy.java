package redis.clone.command;

import redis.clone.KeyValueStore;

public class SetCommandStrategy implements CommandStrategy {
    private final KeyValueStore keyValueStore;

    public SetCommandStrategy(KeyValueStore keyValueStore) {
        this.keyValueStore = keyValueStore;
    }

    @Override
    public String execute(String[] args) {
        if (args.length != 2) {
            return "-ERR wrong number of arguments for 'set' command\r\n";
        }
        return keyValueStore.set(args[0], args[1]);
    }
}