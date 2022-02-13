package server.cache;

import server.dto.Food;

public interface Cache<Type> {
    boolean get(Food food);

    void set(Food food);

    long size();

    void clear();

    boolean isEmpty();
}
