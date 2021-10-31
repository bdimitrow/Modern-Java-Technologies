package bg.sofia.uni.fmi.mjt.spotify.exceptions;

import javax.naming.AuthenticationException;

public class AccountNotFoundException extends Throwable {
    public AccountNotFoundException(){
        super("Account not found!");
    }
}
