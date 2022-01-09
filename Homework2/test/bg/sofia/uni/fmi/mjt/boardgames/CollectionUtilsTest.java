package bg.sofia.uni.fmi.mjt.boardgames;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CollectionUtilsTest {

    @Test
    void testUnion() {
        Collection<Integer> c1 = List.of(1, 2, 3, 4);
        Collection<Integer> c2 = List.of(3, 4, 5, 6);
        Collection<Integer> actual = CollectionUtils.union(c1, c2);

        assertEquals(List.of(1, 2, 3, 4, 5, 6), actual);
    }

    @Test
    void testIntersection() {
        Collection<Integer> c1 = List.of(1, 2, 3, 4);
        Collection<Integer> c2 = List.of(3, 4, 5, 6);
        Collection<Integer> actual = CollectionUtils.intersection(c1, c2);

        assertEquals(List.of(3, 4), actual);
    }

    @Test
    void testDifference() {
        Collection<Integer> c1 = List.of(1, 2, 3, 4);
        Collection<Integer> c2 = List.of(3, 4, 5, 6);
        Collection<Integer> actual = CollectionUtils.difference(c1, c2);

        assertEquals(List.of(1, 2), actual);
    }

    @Test
    void testTolowerCase() {
        Collection<String> original = List.of("Hello", "heLLo", "HELLO", "hEllO", "hello");
        Collection<String> actual = CollectionUtils.toLowerCase(original);
        Collection<String> expected = List.of("hello", "hello", "hello", "hello", "hello");

        assertEquals(expected, actual);
    }
}