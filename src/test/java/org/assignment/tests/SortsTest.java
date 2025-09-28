package org.assignment.tests;

import org.assignment.sorts.Sorts;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Random;

public class SortsTest {
    @Test
    public void testMergeSortRandom() {
        Random r = new Random(1);
        int[] a = r.ints(1000, -5000, 5000).toArray();
        int[] copy = a.clone();
        Arrays.sort(copy);
        Sorts.mergeSort(a);
        assertArrayEquals(copy, a);
    }

    @Test
    public void testQuickSortAdversarial() {
        int n = 1000;
        int[] a = new int[n];
        for (int i=0;i<n;i++) a[i] = i; // sorted - adversarial for naive QS
        int[] copy = a.clone();
        Arrays.sort(copy);
        Sorts.quickSort(a);
        assertArrayEquals(copy, a);
    }

    @Test
    public void testSelect() {
        Random r = new Random(2);
        int[] a = r.ints(1000, 0, 10000).toArray();
        int k = 400;
        int expected = Arrays.stream(a).sorted().toArray()[k];
        int got = Sorts.deterministicSelect(a.clone(), k);
        assertEquals(expected, got);
    }
}
