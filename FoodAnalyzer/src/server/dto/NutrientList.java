package server.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class NutrientList {
    private final Nutrient calories;
    private final Nutrient protein;
    private final Nutrient fat;
    private final Nutrient carbohydrates;
    private final Nutrient fiber;

    public NutrientList(Nutrient calories,
                        Nutrient protein,
                        Nutrient fat,
                        Nutrient carbohydrates,
                        Nutrient fiber) {
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
    }

    public Nutrient getCalories() {
        return calories;
    }

    public Nutrient getProtein() {
        return protein;
    }

    public Nutrient getFat() {
        return fat;
    }

    public Nutrient getCarbohydrates() {
        return carbohydrates;
    }

    public Nutrient getFiber() {
        return fiber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NutrientList that = (NutrientList) o;
        return Objects.equals(calories, that.calories) && Objects.equals(protein, that.protein) && Objects.equals(fat, that.fat) && Objects.equals(carbohydrates, that.carbohydrates) && Objects.equals(fiber, that.fiber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calories, protein, fat, carbohydrates, fiber);
    }

    @Override
    public String toString() {
        return "NutrientList{" +
                "calories=" + calories.getValue() +
                ", protein=" + protein.getValue() +
                ", fat=" + fat.getValue() +
                ", carbohydrates=" + carbohydrates.getValue() +
                ", fiber=" + fiber.getValue() +
                '}';
    }
}
