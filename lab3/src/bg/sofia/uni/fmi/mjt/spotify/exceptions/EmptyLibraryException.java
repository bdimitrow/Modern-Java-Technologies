package bg.sofia.uni.fmi.mjt.spotify.exceptions;

public class EmptyLibraryException extends Throwable {
    public EmptyLibraryException(){
        super("Empty library!");
    }
}
