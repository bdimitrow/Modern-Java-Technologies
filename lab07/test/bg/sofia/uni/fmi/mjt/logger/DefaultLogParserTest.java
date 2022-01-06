package bg.sofia.uni.fmi.mjt.logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultLogParserTest {
    private DefaultLogParser logParser;
    private Path logsFilePath;

    @BeforeEach
    void setUp() throws IOException {
        logsFilePath = Path.of("logs-0.txt");
        logParser = new DefaultLogParser(logsFilePath);
    }

    @Test
    void testInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> logParser.getLogs(null));
        assertThrows(IllegalArgumentException.class, () -> logParser.getLogs(null, null));
        assertThrows(IllegalArgumentException.class, () -> logParser.getLogsTail(-5));

    }

    @Test
    void getLogs() {
        List<Log> result = logParser.getLogs(Level.INFO);

        assertEquals(3, result.size());
        assertTrue(result.contains(new Log(Level.INFO, LocalDateTime.of(2021, 8, 27, 1, 11, 04), "com.cryptobank.investment", "test5")));
    }

    @Test
    void testGetLogs() {
        List<Log> result = logParser.getLogs(LocalDateTime.of(2021, 9, 27, 1, 11, 04), LocalDateTime.of(2022, 8, 27, 1, 11, 04));

        assertEquals(7, result.size());
    }

    @Test
    void getLogsTail() {
        List<Log> result = logParser.getLogsTail(2);

        assertEquals(2, result.size());
        assertTrue(result.contains(new Log(Level.INFO, LocalDateTime.of(2021, 8, 27, 1, 11, 04), "com.cryptobank.investment", "test5")));
    }

    @Test
    void getLogsTailGreaterN() {
        List<Log> result = logParser.getLogsTail(100);

        assertEquals(9, result.size());
        assertTrue(result.contains(new Log(Level.INFO, LocalDateTime.of(2021, 8, 27, 1, 11, 04), "com.cryptobank.investment", "test5")));
    }
}