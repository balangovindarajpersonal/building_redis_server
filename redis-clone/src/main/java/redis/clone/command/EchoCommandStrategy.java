package redis.clone.command;

import java.util.Arrays;

public class EchoCommandStrategy implements CommandStrategy {
    @Override
    public String execute(String[] args) {
        // Skip the command part and re-join the rest to form the message
        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        return "+" + message + "\r\n";
    }
}
