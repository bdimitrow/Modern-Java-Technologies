package server.cache;

import server.dto.Food;
import server.dto.FoodList;

import java.util.LinkedHashSet;
import java.util.Set;

public class LRUCacheFood implements Cache {
    private static final int MAX_CAPACITY = 1000;
    private final Set<Food> cache;
    private int capacity;

    public LRUCacheFood() {
        this.cache = new LinkedHashSet<>(MAX_CAPACITY);
    }

    public LRUCacheFood(int size) {
        this.capacity = size;
        this.cache = new LinkedHashSet<>(capacity);
    }

    public Set<Food> getCache() {
        return cache;
    }

    @Override
    public boolean get(Food food) {
        if (!cache.contains(food))
            return false;
        cache.remove(food);
        cache.add(food);
        return true;
    }

    public FoodList getByKeywords(String keywords) {
        FoodList result = new FoodList();
        for (Food f : cache) {
            if (f.getDescription().matches(keywords)) {
                result.addFoods(Set.of(f));
            }
        }
        return result;
    }

    public Food getByUpcCode(String code) {
        for (Food f : cache) {
            if (f.getGtinUpc().equals(code)) {
                return f;
            }
        }
        return null;
    }

    @Override
    public void set(Food foodToAdd) {
        if (cache.size() == capacity) {
            Food firstKey = cache.iterator().next();
            cache.remove(firstKey);
        }

        cache.add(foodToAdd);
    }

    @Override
    public long size() {
        return this.cache.size();
    }

    @Override
    public void clear() {
        this.cache.clear();
    }

    @Override
    public boolean isEmpty() {
        return this.cache.isEmpty();
    }
}

