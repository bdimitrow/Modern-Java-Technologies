package bg.sofia.uni.fmi.mjt.boardgames;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void union() {
        Collection<Integer> c1 = List.of(1,2,3,4);
        Collection<Integer> c2 = List.of(3,4,5,6);
        Collection<Integer> actual = Utils.union(c1,c2);

        assertEquals(List.of(1,2,3,4,5,6),actual);
    }

    @Test
    void intersection() {
        Collection<Integer> c1 = List.of(1,2,3,4);
        Collection<Integer> c2 = List.of(3,4,5,6);
        Collection<Integer> actual = Utils.intersection(c1,c2);

        assertEquals(List.of(3,4),actual);
    }

    @Test
    void difference() {
        Collection<Integer> c1 = List.of(1,2,3,4);
        Collection<Integer> c2 = List.of(3,4,5,6);
        Collection<Integer> actual = Utils.difference(c1,c2);

        assertEquals(List.of(1,2),actual);
    }
}