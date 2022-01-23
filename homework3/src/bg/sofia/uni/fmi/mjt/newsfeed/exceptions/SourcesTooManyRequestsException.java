package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class SourcesTooManyRequestsException extends Exception {
    public SourcesTooManyRequestsException(String message) {
        super(message);
    }

    public SourcesTooManyRequestsException(String message, Throwable e) {
        super(message, e);
    }
}