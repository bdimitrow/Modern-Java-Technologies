package bg.sofia.uni.fmi.mjt.spotify.exceptions;

import javax.naming.AuthenticationException;

public class PlayableNotFoundException extends Throwable {
    public PlayableNotFoundException(){
        super("Account not found!");
    }
}
