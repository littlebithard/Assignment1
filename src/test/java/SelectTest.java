import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelectTest {

    private final Random random = new Random();

    @DisplayName("Compare result with Arrays.sort(a)[k] across 100 random trials")
    @RepeatedTest(100)
    void testSelectAgainstStandardSort() {
        int size = random.nextInt(1000) + 1;
        int[] arr = random.ints(size, -5000, 5000).toArray();
        int k = random.nextInt(size);

        int[] sortedCopy = Arrays.copyOf(arr, arr.length);
        Arrays.sort(sortedCopy);
        int expected = sortedCopy[k];

        int actual = Select.select(arr, k, new Metrics("Test"));

        assertEquals(expected, actual, "The k-th element found by select was incorrect.");
    }

    @Test
    @DisplayName("Test finding the minimum (k=0) and maximum (k=n-1) elements")
    void testFindingMinAndMax() {
        int[] arr = random.ints(200, 0, 1000).toArray();

        int expectedMin = Arrays.stream(arr).min().getAsInt();
        int actualMin = Select.select(arr, 0, new Metrics("Test"));
        assertEquals(expectedMin, actualMin, "Did not find the minimum element correctly.");

        int expectedMax = Arrays.stream(arr).max().getAsInt();
        int actualMax = Select.select(arr, arr.length - 1, new Metrics("Test"));
        assertEquals(expectedMax, actualMax, "Did not find the maximum element correctly.");
    }
}
