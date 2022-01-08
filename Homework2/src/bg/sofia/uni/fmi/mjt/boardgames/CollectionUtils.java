package bg.sofia.uni.fmi.mjt.boardgames;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CollectionUtils {
    public static <T> Collection<T> union(Collection<T> collection1, Collection<T> collection2) {
        Set<T> set = new HashSet<T>();

        set.addAll(collection1);
        set.addAll(collection2);

        return new ArrayList<T>(set);
    }

    public static <T> Collection<T> intersection(Collection<T> collection1, Collection<T> collection2) {
        Collection<T> result = new ArrayList<T>();

        for (T t : collection1) {
            if (collection2.contains(t)) {
                result.add(t);
            }
        }

        return result;
    }

    public static <T> Collection<T> difference(Collection<T> collection1, Collection<T> collection2) {
        Collection<T> result = new ArrayList<T>();

        for (T t : collection1) {
            if (!collection2.contains(t)) {
                result.add(t);
            }
        }

        return result;
    }

    public static Collection<String> toLowerCase(Collection<String> input) {
        return input.stream()
                .map(String::toLowerCase)
                .toList();
    }
}
