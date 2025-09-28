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

    private static int medianOfFiveIndex(int[] a, int l, int r) {
        int n = r - l + 1;
        int[] idx = new int[n];
        for (int t = 0; t < n; t++) idx[t] = l + t;
        Integer[] boxed = new Integer[n];
        for (int t = 0; t < n; t++) boxed[t] = idx[t];
        Arrays.sort(boxed, (i, j) -> Integer.compare(a[i], a[j]));
        return boxed[n / 2];
    }
    
    private static int findPivot(int[] a, int left, int right, Metrics metrics) {
        int len = right - left + 1;
        if (len <= 5) {
            return medianOfFiveIndex(a, left, right);
        }
        int groups = (len + 4) / 5;
        for (int i = 0; i < groups; i++) {
            int l = left + i * 5;
            int r = Math.min(l + 4, right);
            int m = medianOfFiveIndex(a, l, r);
            swap(a, left + i, m);
        }
        int mid = left + (groups - 1) / 2;
        return selectIndexByValue(a, left, left + groups - 1, mid, metrics);
    }

    private static int selectIndexByValue(int[] a, int l, int r, int kIdx, Metrics metrics) {
        // lightweight Quickselect on indices range
        int left = l, right = r;
        while (true) {
            if (left == right) return left;
            int pivot = a[(left + right) >>> 1];
            int i = left, j = right;
            while (i <= j) {
                while (a[i] < pivot) { metrics.incrementComparisons(); i++; }
                while (a[j] > pivot) { metrics.incrementComparisons(); j--; }
                if (i <= j) {
                    swap(a, i, j);
                    i++; j--;
                }
            }
            if (kIdx <= j) right = j;
            else if (kIdx >= i) left = i;
            else return kIdx;
        }
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