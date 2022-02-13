package server.dto;

import java.util.Objects;

public class NutrientList {
    private Nutrient caloeries; // energiina stonost
    private Nutrient protein; // beltyci
    private Nutrient fat; //maznini
    private Nutrient carbohydrates; //vuglehidrati
    private Nutrient fiber; //fibri

    public NutrientList(Nutrient caloeries,
                        Nutrient protein,
                        Nutrient fat,
                        Nutrient carbohydrates,
                        Nutrient fiber) {
        this.caloeries = caloeries;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
    }

    public Nutrient getCaloeries() {
        return caloeries;
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
        return Objects.equals(caloeries, that.caloeries) && Objects.equals(protein, that.protein) && Objects.equals(fat, that.fat) && Objects.equals(carbohydrates, that.carbohydrates) && Objects.equals(fiber, that.fiber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caloeries, protein, fat, carbohydrates, fiber);
    }

    @Override
    public String toString() {
        return "NutrientList{" +
                "caloeries=" + caloeries.getValue() +
                ", protein=" + protein.getValue() +
                ", fat=" + fat.getValue() +
                ", carbohydrates=" + carbohydrates.getValue() +
                ", fiber=" + fiber.getValue() +
                '}';
    }
}
