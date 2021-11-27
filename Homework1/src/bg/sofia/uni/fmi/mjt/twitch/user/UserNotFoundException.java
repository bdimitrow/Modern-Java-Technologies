package bg.sofia.uni.fmi.mjt.twitch.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message){
        super(message);
    }
}
