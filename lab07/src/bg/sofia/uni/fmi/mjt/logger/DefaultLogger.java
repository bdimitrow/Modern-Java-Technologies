package bg.sofia.uni.fmi.mjt.logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class DefaultLogger implements Logger {
    private LoggerOptions loggerOptions;
    private static int fileName = 0;

    public DefaultLogger(LoggerOptions logOpt) {
        this.loggerOptions = logOpt;
    }


    @Override
    public void log(Level level, LocalDateTime timestamp, String message) {
        if (level == null || timestamp == null || message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Illegal arguments are passed to 'log' function.");
        }
        if (this.loggerOptions.shouldThrowErrors()) {
            throw new LogException("Logger should throw errors.");
        }

        if ((this.loggerOptions.getDirectory() + fileName).length() >= loggerOptions.getMaxFileSizeBytes()) {
            ++fileName;
        }
        try (OutputStream os = new FileOutputStream(this.loggerOptions.getDirectory() + "/logs-" + fileName + ".txt")) {
            if (this.loggerOptions.getMinLogLevel().getLevel() <= level.getLevel()) {
                String toBeWritten = "[" + level.name() + "]|" +
                        timestamp.toString() + "|" +
                        this.loggerOptions.getClazz() + "|" +
                        message +
                        System.lineSeparator();

//                Log forWrite = new Log()

                os.write(toBeWritten.getBytes());
            }
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
        return Path.of(this.loggerOptions.getDirectory() + "/logs-" + fileName + ".txt");
    }
}
