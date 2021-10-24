import java.util.Arrays;

public class ArrayAnalyzer {
    public static void main(String[] args) {
        System.out.println(isMountainArray(new int[]{2, 1}));
        System.out.println(isMountainArray(new int[]{3, 5, 5}));
        System.out.println(isMountainArray(new int[]{0, 3, 2, 1}));
        System.out.println(isMountainArrayOptimized(new int[]{2, 1}));
        System.out.println(isMountainArrayOptimized(new int[]{3, 5, 5}));
        System.out.println(isMountainArrayOptimized(new int[]{0, 3, 2, 1}));
    }


    private static boolean isMountainArrayOptimized(int[] array) {
        if (array.length < 3) {
            return false;
        }
        if (array[0] > array[1]) return false;
        int i = 1;
        while (array[i - 1] < array[i]) {
            ++i;
        }
        for (; i < array.length; ++i) {
            if (array[i - 1] <= array[i]) {
                return false;
            }
        }
        return true;
    }


    private static boolean isMountainArray(int[] array) {
        if (array.length < 3) {
            return false;
        }
        int peak = Arrays.stream(array).max().getAsInt();
        int position = 0;
        for (var i : array) {
            if (i == peak) {
                break;
            }
            ++position;
        }
        if (array[0] > array[1]) return false;

        for (int i = position; i > 0; i--) {
            if (array[i - 1] >= array[i]) {
                return false;
            }
        }
        for (int i = position; i < array.length - 1; i++) {
            if (array[i] <= array[i + 1]) {
                return false;
            }
        }
        return true;
    }
}
