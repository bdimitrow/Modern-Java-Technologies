package bg.sofia.uni.fmi.mjt.gifts.exception;

public class WrongReceiverException extends Exception {
    public WrongReceiverException() {
        super("The receiver is different from the receiver of the gift.");
    }
}
