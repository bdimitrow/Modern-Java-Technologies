package server.dto;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class FoodList {
    private int totalHits;
    private Set<Food> foods;

    public FoodList() {
        this.foods = new LinkedHashSet<>();
    }

    public FoodList(Set<Food> foods, int totalHits) {
        this.foods = foods;
        this.totalHits = totalHits;
    }

    public boolean isEmpty() {
        return foods.isEmpty();
    }

    public Set<Food> getFoods() {
        return this.foods;
    }

    public void addFoods(Set<Food> other) {
        this.foods.addAll(other);
    }

    public int getTotalHits() {
        return this.totalHits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodList foodList = (FoodList) o;
        return Objects.equals(foods, foodList.foods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foods);
    }

    @Override
    public String toString() {
        return "FoodList: "
               + foods;
    }
}
