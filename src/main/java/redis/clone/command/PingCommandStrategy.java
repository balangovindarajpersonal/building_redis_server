package redis.clone.command;

public class PingCommandStrategy implements CommandStrategy {
    @Override
    public String execute(String[] args) {
        return "+PONG\r\n";
    }
}

