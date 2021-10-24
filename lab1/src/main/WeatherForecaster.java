public class WeatherForecaster {
    private static int[] getsWarmerIn(int[] temperatures) {
        int[] result = new int[temperatures.length];
        for (int i = 0; i < temperatures.length - 1; ++i) {
            result[i] = findNextHotter(temperatures, i);
        }
        result[temperatures.length - 1] = 0;
        return result;
    }

    private static int findNextHotter(int[] arr, int currentIndex) {
        for (int i = currentIndex; i < arr.length; ++i) {
            if (arr[i] > arr[currentIndex]) {
                return i - currentIndex;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        int[] test1 = getsWarmerIn(new int[]{73, 74, 75, 71, 69, 72, 76, 73});
        for (var i : test1) {
            System.out.println(i);
        }


    }


}