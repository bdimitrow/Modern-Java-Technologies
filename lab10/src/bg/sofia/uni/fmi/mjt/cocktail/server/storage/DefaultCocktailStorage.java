package bg.sofia.uni.fmi.mjt.cocktail.server.storage;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DefaultCocktailStorage implements CocktailStorage {
    private final Set<Cocktail> cocktailSet;

    public DefaultCocktailStorage() {
        cocktailSet = new HashSet<>();
    }

    @Override
    public void createCocktail(Cocktail cocktail) throws CocktailAlreadyExistsException {
        for (Cocktail current : cocktailSet) {
            if (current.equals(cocktail)) {
                throw new CocktailAlreadyExistsException("Cocktail already exists!");
            }
        }
        cocktailSet.add(cocktail);
    }

    @Override
    public Collection<Cocktail> getCocktails() {
        return cocktailSet;
    }

    @Override
    public Collection<Cocktail> getCocktailsWithIngredient(String ingredientName) {
        Collection<Cocktail> result = new ArrayList<>();
        for (Cocktail current : cocktailSet) {
            for (Ingredient ingredientOfCurrent : current.ingredients()) {
                if (ingredientOfCurrent.name().equals(ingredientName)) {
                    result.add(current);
                }
            }
        }

        return result;
    }

    @Override
    public Cocktail getCocktail(String name) throws CocktailNotFoundException {
        Cocktail result = null;
        for (Cocktail current : cocktailSet) {
            if (current.name().equals(name)) {
                result = current;
            }
        }

        if (result == null) {
            throw new CocktailNotFoundException("Such a cocktail does not exist!");
        }

        return result;
    }
}
