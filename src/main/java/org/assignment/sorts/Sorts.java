package org.assignment.sorts;

import org.assignment.util.Metrics;

import java.util.Random;
import java.util.Arrays;

public class Sorts {
    private static final Random RNG = new Random(12345);

    // MergeSort public API
    public static void mergeSort(int[] a) {
        if (a == null) return;
        int[] buf = new int[a.length];
        mergeSort(a, buf, 0, a.length);
    }

    private static void mergeSort(int[] a, int[] buf, int lo, int hi) {
        Metrics.enter();
        try {
            int n = hi - lo;
            if (n <= 16) { insertionSort(a, lo, hi); return; }
            int mid = (lo + hi) >>> 1;
            mergeSort(a, buf, lo, mid);
            mergeSort(a, buf, mid, hi);
            int i = lo, j = mid, k = lo;
            while (i < mid && j < hi) {
                Metrics.cmp();
                if (a[i] <= a[j]) buf[k++] = a[i++]; else buf[k++] = a[j++];
            }
            while (i < mid) buf[k++] = a[i++];
            while (j < hi) buf[k++] = a[j++];
            System.arraycopy(buf, lo, a, lo, n);
        } finally {
            Metrics.exit();
        }
    }

    private static void insertionSort(int[] a, int lo, int hi) {
        for (int i = lo + 1; i < hi; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= lo) {
                Metrics.cmp();
                if (a[j] <= key) break;
                a[j+1] = a[j--];
            }
            a[j+1] = key;
        }
    }

    // QuickSort public API
    public static void quickSort(int[] a) {
        if (a == null) return;
        quickSort(a, 0, a.length-1);
    }

    private static void quickSort(int[] a, int lo, int hi) {
        int curLo = lo, curHi = hi;
        while (curLo < curHi) {
            Metrics.enter();
            try {
                if (curHi - curLo <= 16) { insertionSort(a, curLo, curHi+1); return; }
                int pivotIndex = curLo + RNG.nextInt(curHi - curLo + 1);
                int pivot = a[pivotIndex];
                int p = partition(a, curLo, curHi, pivot);
                if (p - curLo < curHi - p) {
                    quickSort(a, curLo, p-1);
                    curLo = p+1;
                } else {
                    quickSort(a, p+1, curHi);
                    curHi = p-1;
                }
            } finally {
                Metrics.exit();
            }
        }
    }

    private static int partition(int[] a, int lo, int hi, int pivot) {
        int i = lo, j = hi;
        while (i <= j) {
            while (i <= j) {
                Metrics.cmp(); if (a[i] < pivot) i++; else break;
            }
            while (i <= j) {
                Metrics.cmp(); if (a[j] > pivot) j--; else break;
            }
            if (i <= j) {
                int t = a[i]; a[i] = a[j]; a[j] = t;
                i++; j--;
            }
        }
        return i;
    }

    // Deterministic select (median of medians) - returns k-th smallest (0-based)
    public static int deterministicSelect(int[] a, int k) {
        if (a == null || k < 0 || k >= a.length) throw new IllegalArgumentException();
        return select(a, 0, a.length-1, k);
    }

    private static int select(int[] a, int lo, int hi, int k) {
        Metrics.enter();
        try {
            while (lo <= hi) {
                if (hi - lo + 1 <= 16) {
                    Arrays.sort(a, lo, hi+1);
                    return a[lo + k];
                }
                int pivot = medianOfMedians(a, lo, hi);
                int p = partition(a, lo, hi, pivot);
                int leftSize = p - lo;
                if (k < leftSize) {
                    hi = p-1;
                } else if (k > leftSize) {
                    k = k - leftSize;
                    lo = p;
                } else {
                    return pivot;
                }
            }
            throw new IllegalStateException();
        } finally {
            Metrics.exit();
        }
    }

    private static int medianOfMedians(int[] a, int lo, int hi) {
        int n = hi - lo + 1;
        int numMedians = (n + 4) / 5;
        for (int i = 0; i < numMedians; i++) {
            int subLo = lo + i*5;
            int subHi = Math.min(subLo + 4, hi);
            insertionSort(a, subLo, subHi+1);
            int medianIndex = subLo + (subHi - subLo) / 2;
            int target = lo + i;
            int tmp = a[target]; a[target] = a[medianIndex]; a[medianIndex] = tmp;
        }
        return selectPivot(a, lo, lo + numMedians - 1);
    }

    private static int selectPivot(int[] a, int lo, int hi) {
        int len = hi - lo + 1;
        if (len == 1) return a[lo];
        int mid = lo + len/2;
        return select(a, lo, hi, mid - lo);
    }
}
