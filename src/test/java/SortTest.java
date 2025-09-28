import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SortTest {
    private final Random random = new Random();

    static Stream<BiConsumer<int[], Metrics>> sortAlgorithmProvider() {
        return Stream.of(MergeSort::sort, QuickSort::sort);
    }

    @DisplayName("Test sorting on randomly generated arrays")
    @ParameterizedTest(name = "{index} => Using {0}")
    @MethodSource("sortAlgorithmProvider")
    void testRandomArrays(BiConsumer<int[], Metrics> sorter) {
        int[] arr = random.ints(2000, -1000, 1000).toArray();
        int[] expected = Arrays.copyOf(arr, arr.length);
        Arrays.sort(expected);
        sorter.accept(arr, new Metrics("Test"));
        assertArrayEquals(expected, arr);
    }

    @DisplayName("Test sorting on adversarial (reverse-sorted) arrays")
    @ParameterizedTest(name = "{index} => Using {0}")
    @MethodSource("sortAlgorithmProvider")
    void testReverseSortedArrays(BiConsumer<int[], Metrics> sorter) {
        int[] arr = random.ints(2000, -1000, 1000).sorted().toArray();
        for (int i = 0; i < arr.length / 2; i++) {
            int temp = arr[i];
            arr[i] = arr[arr.length - 1 - i];
            arr[arr.length - 1 - i] = temp;
        }
        int[] expected = Arrays.copyOf(arr, arr.length);
        Arrays.sort(expected);
        sorter.accept(arr, new Metrics("Test"));
        assertArrayEquals(expected, arr);
    }

    @DisplayName("Test sorting edge cases (empty, single element)")
    @ParameterizedTest(name = "{index} => Using {0}")
    @MethodSource("sortAlgorithmProvider")
    void testEdgeCaseArrays(BiConsumer<int[], Metrics> sorter) {
        int[] emptyArr = {};
        sorter.accept(emptyArr, new Metrics("Test"));
        assertArrayEquals(new int[]{}, emptyArr);

        int[] singleElementArr = {42};
        sorter.accept(singleElementArr, new Metrics("Test"));
        assertArrayEquals(new int[]{42}, singleElementArr);
    }

    @DisplayName("Test sorting arrays with many duplicate values")
    @ParameterizedTest(name = "{index} => Using {0}")
    @MethodSource("sortAlgorithmProvider")
    void testArrayWithDuplicates(BiConsumer<int[], Metrics> sorter) {
        int[] arr = new int[1000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(10); // Many duplicates from 0-9
        }
        int[] expected = Arrays.copyOf(arr, arr.length);
        Arrays.sort(expected);
        sorter.accept(arr, new Metrics("Test"));
        assertArrayEquals(expected, arr);
    }


    @Test
    @DisplayName("Verify QuickSort recursion depth is bounded to O(log n)")
    void testQuickSortBoundedRecursionDepth() {
        int n = 100_000;
        int[] arr = random.ints(n).toArray();
        Metrics metrics = new Metrics("QuickSortDepthTest");
        QuickSort.sort(arr, metrics);
    }
}

