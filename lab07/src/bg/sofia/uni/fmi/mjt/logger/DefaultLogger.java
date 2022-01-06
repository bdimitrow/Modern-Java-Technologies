package bg.sofia.uni.fmi.mjt.logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class DefaultLogger implements Logger {
    private final LoggerOptions loggerOptions;
    private int fileName = 0;
    private Path currentPath;

    public DefaultLogger(LoggerOptions logOpt) {
        this.loggerOptions = logOpt;
        this.fileName = 0;
        this.currentPath = Paths.get(loggerOptions.getDirectory(), "logs-" + fileName + ".txt");
        try {
            if (!Files.exists(currentPath)) {
                Files.createFile(currentPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new LogException("Error while creating file.");
        }
    }


    @Override
    public void log(Level level, LocalDateTime timestamp, String message) {
        if (level == null || timestamp == null || message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Illegal arguments are passed to 'log' function.");
        }
        if (this.loggerOptions.shouldThrowErrors()) {
            throw new LogException("Logger should throw errors.");
        }

        try {
            if (Files.exists(this.currentPath) &&
                    Files.size(this.currentPath) >= this.loggerOptions.getMaxFileSizeBytes()) {
                updateLogFile();
            }
        } catch (IOException e) {
            throw new LogException("Could not update log file.");
        }

        try (OutputStream os = new FileOutputStream(String.valueOf(this.currentPath), true)) {
            if (this.loggerOptions.getMinLogLevel().getLevel() > level.getLevel()) {
                return;
            }
            String toBeWritten = "[" + level.name() + "]|" +
                    timestamp.toString() + "|" +
                    this.loggerOptions.getClazz().getPackageName() + "|" +
                    message +
                    System.lineSeparator();

            os.write(toBeWritten.getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new LogException("Operation could not be performed.");
        }
    }

    @Override
    public LoggerOptions getOptions() {
        return this.loggerOptions;
    }

    @Override
    public Path getCurrentFilePath() {
        return currentPath;
    }

    private void updateLogFile() {
        ++fileName;
        this.currentPath = Paths.get(loggerOptions.getDirectory(), "logs-" + fileName + ".txt");
    }
}
