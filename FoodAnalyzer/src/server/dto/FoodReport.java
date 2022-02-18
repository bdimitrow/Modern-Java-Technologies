package server.dto;

import java.util.List;
import java.util.Objects;

public class FoodReport {
    private final String description;
    private final String ingredients;
    private final int fdcId;
    private final List<FoodNutrient> foodNutrients;

    public FoodReport(String description, String ingredients, int fdcId, List<FoodNutrient> nutrientList) {
        this.description = description;
        this.ingredients = ingredients;
        this.fdcId = fdcId;
        this.foodNutrients = nutrientList;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public int getFdcId() {
        return fdcId;
    }

    public List<FoodNutrient> getNutrientList() {
        return foodNutrients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodReport that = (FoodReport) o;
        return fdcId == that.fdcId &&
               Objects.equals(description, that.description) &&
               Objects.equals(ingredients, that.ingredients) &&
               Objects.equals(foodNutrients, that.foodNutrients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, ingredients, fdcId, foodNutrients);
    }

    @Override
    public String toString() {
        return "FoodReport{" +
               "description='" + description + '\'' +
               ", ingredients='" + ingredients + '\'' +
               ", fdcId='" + fdcId + '\'' +
               ", nutrientList='" + getNutrients() + '\'' +
               '}';
    }

    public String getNutrients() {
        if (foodNutrients == null) return null;
        StringBuilder res = new StringBuilder();
        for (FoodNutrient nutrient : foodNutrients)
            if (nutrient.getNutrient().getName().contains("Energy") ||
                nutrient.getNutrient().getName().contains("Protein") ||
                nutrient.getNutrient().getName().contains("lipid") ||
                nutrient.getNutrient().getName().contains("Carbohydrate") ||
                nutrient.getNutrient().getName().contains("Fiber")) {
                res.append(nutrient.getNutrient().getName());
                res.append("='");
                res.append(nutrient.getAmount());
                res.append(nutrient.getNutrient().getUnitName());
                res.append("' ");
            }
        return res.toString();
    }

}

