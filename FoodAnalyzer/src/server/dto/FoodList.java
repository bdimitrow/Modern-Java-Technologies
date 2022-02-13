package server.dto;

import java.util.Objects;
import java.util.Set;

public class FoodList {
    private Set<Food> foods;

    public FoodList(Set<Food> foods) {
        this.foods = foods;
    }

    public Set<Food> getFoods() {
        return foods;
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
