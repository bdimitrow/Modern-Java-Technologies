package bg.sofia.uni.fmi.mjt.math;

public class MathUtils {
    public static boolean isPrime(int n) {

//        throw new UnsupportedOperationException("Not yet implemented");
        if (n < 2) {
            throw new IllegalArgumentException("n must be > 1");
        }

        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }
}
