package redis.clone;

import java.util.concurrent.ConcurrentHashMap;

public class KeyValueStore {
    private volatile static KeyValueStore instance = null;

    private final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();

    private KeyValueStore() {
    }

    public static KeyValueStore getInstance() {
        if (instance == null) {
            synchronized (KeyValueStore.class) {
                if (instance == null) {
                    instance = new KeyValueStore();
                }
            }
        }
        return instance;
    }

    public String set(String key, String value) {
        store.put(key, value);
        return "+OK\r\n";
    }

    public String get(String key) {
        String value = store.get(key);
        if (value != null) {
            return "$" + value.length() + "\r\n" + value + "\r\n";
        } else {
            return "$-1\r\n";
        }
    }
}