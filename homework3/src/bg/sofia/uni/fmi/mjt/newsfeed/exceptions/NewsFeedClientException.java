package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class NewsFeedClientException extends Exception {
    public NewsFeedClientException(String message) {
        super(message);
    }

    public NewsFeedClientException(String message, Throwable e) {
        super(message, e);
    }
}
