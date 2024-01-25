package redis.clone.command;

public interface CommandStrategy {
    String execute(String[] args);
}
