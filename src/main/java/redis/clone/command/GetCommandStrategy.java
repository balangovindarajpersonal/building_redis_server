package redis.clone.command;

import redis.clone.datastructure.KeyValueStore;

public class GetCommandStrategy implements CommandStrategy {
    private final KeyValueStore keyValueStore;

    public GetCommandStrategy(KeyValueStore keyValueStore) {
        this.keyValueStore = keyValueStore;
    }

    @Override
    public String execute(String[] args) {
        if (args.length != 1) {
            return "-ERR wrong number of arguments for 'get' command\r\n";
        }
        return keyValueStore.get(args[0]);
    }
}