import java.util.Arrays;

public class MergeSort {

    private static final int INSERTION_SORT_CUTOFF = 15;

    public static void sort(int[] array, Metrics metrics) {
        if (array == null || array.length <= 1) {
            return;
        }
        int[] buffer = new int[array.length];
        mergesortRecursive(array, buffer, 0, array.length - 1, metrics);
    }

    private static void mergesortRecursive(int[] array, int[] buffer, int left, int right, Metrics metrics) {
        metrics.enterRecursion();
        if (right - left <= INSERTION_SORT_CUTOFF) {
            insertionSort(array, left, right, metrics);
        } else {
            int mid = left + (right - left) / 2;
            mergesortRecursive(array, buffer, left, mid, metrics);
            mergesortRecursive(array, buffer, mid + 1, right, metrics);
            merge(array, buffer, left, mid, right, metrics);
        }
        metrics.exitRecursion();
    }

    private static void merge(int[] array, int[] buffer, int left, int mid, int right, Metrics metrics) {
        System.arraycopy(array, left, buffer, left, right - left + 1);

        int i = left;
        int j = mid + 1;
        int k = left;

        // Merge back into the original array
        while (i <= mid && j <= right) {
            metrics.incrementComparisons();
            if (buffer[i] <= buffer[j]) {
                array[k++] = buffer[i++];
            } else {
                array[k++] = buffer[j++];
            }
        }

        while (i <= mid) {
            array[k++] = buffer[i++];
        }

    }

    private static void insertionSort(int[] array, int left, int right, Metrics metrics) {
        for (int i = left + 1; i <= right; i++) {
            int current = array[i];
            int j = i - 1;
            while (j >= left && array[j] > current) {
                metrics.incrementComparisons();
                array[j + 1] = array[j];
                j--;
            }
            if (j >= left) {
                metrics.incrementComparisons();
            }
            array[j + 1] = current;
        }
    }
}