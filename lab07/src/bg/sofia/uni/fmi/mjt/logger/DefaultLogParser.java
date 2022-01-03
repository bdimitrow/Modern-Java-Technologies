package bg.sofia.uni.fmi.mjt.logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultLogParser implements LogParser {
    private Path logsFilePath;
    private List<Log> allLogs;

    public DefaultLogParser(Path logsFilePath) {
        this.logsFilePath = logsFilePath;

        try (BufferedReader bufferedReader = Files.newBufferedReader(logsFilePath)) {
            allLogs = bufferedReader.lines().map(Log::of).toList();
        } catch (IOException e) {
            e.printStackTrace();
            allLogs = Collections.emptyList();
        }
    }

    @Override
    public List<Log> getLogs(Level level) {
        if (level == null) {
            throw new IllegalArgumentException("Illegal argument! Level can not be null.");
        }

        List<Log> result = new ArrayList<>();
        for (var log : allLogs) {
            if (log.level() == level) {
                result.add(log);
            }
        }

        return result;
    }

    @Override
    public List<Log> getLogs(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Invalid arguments! Time is undefined.");
        }

        List<Log> result = new ArrayList<>();
        for (var log : allLogs) {
            var currLogTimeStamp = log.timestamp();
            if (currLogTimeStamp.isAfter(from) && log.timestamp().isBefore(to)) {
                result.add(log);
            }
        }

        return result;
    }

    @Override
    public List<Log> getLogsTail(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Invalid arguments! Size of list can not be negative.");
        }

        return allLogs.subList(Math.max(0, allLogs.size() - n), allLogs.size());
    }
}
