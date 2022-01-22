package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class ServerErrorException extends Exception {
    public ServerErrorException(String message) {
        super(message);
    }

    public ServerErrorException(String message, Throwable e) {
        super(message, e);
    }
}
