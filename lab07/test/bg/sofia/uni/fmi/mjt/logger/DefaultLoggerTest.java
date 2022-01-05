package bg.sofia.uni.fmi.mjt.logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultLoggerTest {
    private static final String TEST_DIR = "test_dir";
    private static DefaultLogger defaultLogger;


    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectory(Path.of(TEST_DIR));
        LoggerOptions loggerOptions = new LoggerOptions(this.getClass(), TEST_DIR);
        loggerOptions.setMaxFileSizeBytes(20);
        defaultLogger = new DefaultLogger(loggerOptions);
    }

    @AfterEach
    void tearDown() throws IOException {
        Path path = Path.of(TEST_DIR);
        DirectoryStream<Path> contents = Files.newDirectoryStream(path);
        for (var file : contents) {
            Files.delete(file);
        }
        Files.delete(Path.of(TEST_DIR));
    }

    @Test
    void testLogUnhappyPath() {
        assertThrows(IllegalArgumentException.class, () -> defaultLogger.log(null, LocalDateTime.of(2020, 11, 1, 10, 2), "test"));
        assertThrows(IllegalArgumentException.class, () -> defaultLogger.log(Level.DEBUG, null, "test"));
        assertThrows(IllegalArgumentException.class, () -> defaultLogger.log(Level.DEBUG, LocalDateTime.of(2020, 11, 1, 10, 2), null));
        assertThrows(IllegalArgumentException.class, () -> defaultLogger.log(Level.DEBUG, LocalDateTime.of(2020, 11, 1, 10, 2), ""));
    }

    @Test
    void testLogHappyPath() {
        defaultLogger.log(Level.DEBUG, LocalDateTime.now(), "test");

        List<String> files = this.getContents();
        assertEquals(1, files.size());
        assertEquals("logs-0.txt", files.get(0));
    }

    @Test
    void testLogMoreFiles() {
        for (int i = 0; i < 500; ++i) {
            defaultLogger.log(Level.DEBUG, LocalDateTime.now(), "test");
        }

        List<String> files = this.getContents();

        assertTrue(files.size() > 1);
        assertTrue(files.contains("logs-0.txt"));
        assertTrue(files.contains("logs-1.txt"));
    }

    private List<String> getContents()
    {
        List<String> contents = new ArrayList<>();
        Path path = Path.of(TEST_DIR);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {

            for (Path file : stream) {
                contents.add(file.getFileName().toString());
            }
        } catch (IOException | DirectoryIteratorException e) {
            System.out.println("Error!");
        }

        return contents;
    }
}