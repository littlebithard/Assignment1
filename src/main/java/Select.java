import java.util.Arrays;

public class Select {

    public static int select(int[] array, int k, Metrics metrics) {
        if (array == null || array.length == 0 || k < 0 || k >= array.length) {
            throw new IllegalArgumentException("Invalid input or k is out of bounds.");
        }
        return select(array, 0, array.length - 1, k, metrics);
    }

    private static int select(int[] array, int left, int right, int k, Metrics metrics) {
        while (true) {
            metrics.enterRecursion();
            if (left == right) {
                metrics.exitRecursion();
                return array[left];
            }

            int pivotIndex = findPivot(array, left, right, metrics);
            pivotIndex = partition(array, left, right, pivotIndex, metrics);

            if (k == pivotIndex) {
                metrics.exitRecursion();
                return array[k];
            } else if (k < pivotIndex) {
                right = pivotIndex - 1;
            } else {
                left = pivotIndex + 1;
            }
            metrics.exitRecursion();
        }
    }

    private static int findPivot(int[] array, int left, int right, Metrics metrics) {
        if (right - left < 5) {
            return findMedianOfSubarray(array, left, right, metrics);
        }

        int numGroups = (int) Math.ceil((double) (right - left + 1) / 5);
        int[] medians = new int[numGroups];

        for (int i = 0; i < numGroups; i++) {
            int subLeft = left + i * 5;
            int subRight = Math.min(subLeft + 4, right);
            int medianIndex = findMedianOfSubarray(array, subLeft, subRight, metrics);
            medians[i] = array[medianIndex];
        }

        int medianOfMedians = select(medians, medians.length / 2, new Metrics("MoM_Internal"));

        for (int i = left; i <= right; i++) {
            if (array[i] == medianOfMedians) {
                return i;
            }
        }
        return -1; // Should not happen
    }

    private static int findMedianOfSubarray(int[] array, int left, int right, Metrics metrics) {
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
        return left + (right - left) / 2;
    }

    private static int partition(int[] array, int left, int right, int pivotIndex, Metrics metrics) {
        int pivotValue = array[pivotIndex];
        swap(array, pivotIndex, right);
        int storeIndex = left;

        for (int i = left; i < right; i++) {
            metrics.incrementComparisons();
            if (array[i] < pivotValue) {
                swap(array, storeIndex, i);
                storeIndex++;
            }
        }
        swap(array, right, storeIndex);
        return storeIndex;
    }

    private static void swap(int[] array, int a, int b) {
        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }
}