package bg.sofia.uni.fmi.mjt.spotify.exceptions;

public class LibraryCapacityExceededException extends Throwable {
    public LibraryCapacityExceededException(){
        super("Library capacity exceeded");
    }
}
