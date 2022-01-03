package bg.sofia.uni.fmi.mjt.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Log(Level level, LocalDateTime timestamp, String packageName, String message) {
    private final static int MESSAGE = 3;

    public static Log of(String line) {
        final String[] tokens = line.split("\\|");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-YYYY");
        var level = tokens[0].substring(1, tokens[0].length() - 1);
        return new Log(
                Level.valueOf(level),
                LocalDateTime.parse(tokens[1], formatter),
                tokens[2],
                tokens[MESSAGE]);
    }
}
