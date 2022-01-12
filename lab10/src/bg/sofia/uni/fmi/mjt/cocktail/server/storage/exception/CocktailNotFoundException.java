package bg.sofia.uni.fmi.mjt.cocktail.server.storage.exception;

public class CocktailNotFoundException extends Exception {
    public CocktailNotFoundException(String message) {
        super(message);
    }

    public CocktailNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
