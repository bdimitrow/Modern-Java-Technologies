package bg.sofia.uni.fmi.mjt.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {

    @Test
    void testIsPrimeLessThanOne() {
        assertThrows(IllegalArgumentException.class, ()-> MathUtils.isPrime(-5),"prime is not defined for non-positive number");
    }

    @Test
    void testIsPrimeOne() {
        assertThrows(IllegalArgumentException.class, ()-> MathUtils.isPrime(1),"prime is not defined for numbers 1");
    }

    @Test
    void testIsPrimeWhenPrime() {
        assertTrue(MathUtils.isPrime(13), "13 is prime");
    }

    @Test
    void testIsPrimeWhenNotPrime(){
        assertFalse(MathUtils.isPrime(14), "14 is not prime");
    }

    @Test
    void testIsPrimeBigNumber() {
        assertTrue(MathUtils.isPrime(27644437), "27644437 is prime");
    }
}