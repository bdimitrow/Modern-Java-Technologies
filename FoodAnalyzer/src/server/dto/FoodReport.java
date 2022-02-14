package server.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class FoodReport {
    private final String description;
    private final String ingredients;
    private final int fdcId;
    @SerializedName("foodNutrients")
    private final NutrientList labelNutrients;

    public FoodReport(String description, String ingredients, int fdcId, NutrientList nutrientList) {
        this.description = description;
        this.ingredients = ingredients;
        this.fdcId = fdcId;
        this.labelNutrients = nutrientList;
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

    public NutrientList getNutrientList() {
        return labelNutrients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodReport that = (FoodReport) o;
        return fdcId == that.fdcId && Objects.equals(description, that.description) && Objects.equals(ingredients, that.ingredients) && Objects.equals(labelNutrients, that.labelNutrients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, ingredients, fdcId, labelNutrients);
    }

    @Override
    public String toString() {
        return "FoodReport{" +
                "description='" + description + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", fdcId='" + fdcId + '\'' +
                ", nutrientList='" + labelNutrients +'\'' +
                '}';
    }
}

