package bg.sofia.uni.fmi.mjt.boardgames;

public class FileException extends RuntimeException {
    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable th) {
        super(message, th);
    }
}
