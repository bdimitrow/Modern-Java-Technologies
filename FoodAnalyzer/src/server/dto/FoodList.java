package server.dto;

import java.util.Objects;
import java.util.Set;

public class FoodList {
    private int currentPage;
    private int totalPages;
    private Set<Food> foods;

    public FoodList(int currentPage, int totalPages, Set<Food> foods) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.foods = foods;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public Set<Food> getFoods() {
        return foods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodList foodList = (FoodList) o;
        return currentPage == foodList.currentPage && totalPages == foodList.totalPages && Objects.equals(foods, foodList.foods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPage, totalPages, foods);
    }

    @Override
    public String toString() {
        return "FoodList: "
                + foods;
    }
}
