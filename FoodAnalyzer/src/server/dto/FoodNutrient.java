package server.dto;

import java.util.Objects;

public class FoodNutrient {
    private final Nutrient nutrient;
    private final double amount;

    public FoodNutrient(Nutrient nutrient, double amount) {
        this.nutrient = nutrient;
        this.amount = amount;
    }

    public Nutrient getNutrient() {
        return nutrient;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodNutrient that = (FoodNutrient) o;
        return Double.compare(that.amount, amount) == 0 && Objects.equals(nutrient, that.nutrient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nutrient, amount);
    }

    @Override
    public String toString() {
        return "FoodNutrient{" +
                "nutrient=" + nutrient +
                ", amount=" + amount +
                '}';
    }
}
