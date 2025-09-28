import java.util.Random;

public class QuickSort {

    private static final Random RANDOM = new Random();

    public static void sort(int[] array, Metrics metrics) {
        if (array == null || array.length <= 1) {
            return;
        }
        quicksort(array, 0, array.length - 1, metrics);
    }

    private static void quicksort(int[] array, int left, int right, Metrics metrics) {
        while (left < right) {
            metrics.enterRecursion();
            int pivotIndex = partition(array, left, right, metrics);

            if (pivotIndex - left < right - pivotIndex) {
                quicksort(array, left, pivotIndex - 1, metrics);
                left = pivotIndex + 1;
            } else {
                quicksort(array, pivotIndex + 1, right, metrics);
                right = pivotIndex - 1;
            }
            metrics.exitRecursion();
        }
    }

    private static int partition(int[] array, int left, int right, Metrics metrics) {
        int randomPivotIndex = left + RANDOM.nextInt(right - left + 1);
        swap(array, randomPivotIndex, right);
        int pivotValue = array[right];

        int storeIndex = left;
        for (int i = left; i < right; i++) {
            metrics.incrementComparisons();
            if (array[i] < pivotValue) {
                swap(array, i, storeIndex);
                storeIndex++;
            }
        }

        swap(array, storeIndex, right);
        return storeIndex;
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}