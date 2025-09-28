import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SortVsSelectBenchmark {

    private static final int[] SIZES = {1_000, 10_000, 100_000};
    private static final int WARMUP_ITER = 3;
    private static final int MEASURE_ITER = 5;

    private static int[] sourceArray;
    private static int k;

    private static void setup(int n) {
        sourceArray = new Random(12345).ints(n, 0, n * 10).toArray();
        k = n / 2;
    }

    private static int deterministicSelectRun() {
        int[] workArray = Arrays.copyOf(sourceArray, sourceArray.length);
        return Select.select(workArray, k, new Metrics("Micro_Select"));
    }

    private static int sortAndSelectRun() {
        int[] workArray = Arrays.copyOf(sourceArray, sourceArray.length);
        Arrays.sort(workArray);
        return workArray[k];
    }

    private static long timeNanos(Runnable r) {
        long start = System.nanoTime();
        r.run();
        return System.nanoTime() - start;
    }

    private static double millis(long nanos) {
        return nanos / 1_000_000.0;
    }

    public static void main(String[] args) {
        for (int n : SIZES) {
            setup(n);

            for (int i = 0; i < WARMUP_ITER; i++) {
                deterministicSelectRun();
                sortAndSelectRun();
            }

            long totalNsSelect = 0;
            int check1 = 0;
            for (int i = 0; i < MEASURE_ITER; i++) {
                long ns = timeNanos(() -> {
                    // capture the result to prevent dead code elimination
                    int res = deterministicSelectRun();
                    // store to a field/array (here: closure) via side effect
                    // but we just assign to outer variable after timing
                });
                totalNsSelect += ns;
                check1 = deterministicSelectRun(); // light extra call to read result
            }

            // Measure sortAndSelect
            long totalNsSort = 0;
            int check2 = 0;
            for (int i = 0; i < MEASURE_ITER; i++) {
                long ns = timeNanos(() -> {
                    int res = sortAndSelectRun();
                });
                totalNsSort += ns;
                check2 = sortAndSelectRun();
            }

            if (check1 != check2) {
                System.out.println("Mismatch for N=" + n + ": select=" + check1 + " sort=" + check2);
            }

            double avgMsSelect = millis(totalNsSelect) / MEASURE_ITER;
            double avgMsSort = millis(totalNsSort) / MEASURE_ITER;

            System.out.printf("N=%d | deterministicSelect ~ %.3f ms | sortAndSelect ~ %.3f ms%n",
                    n, avgMsSelect, avgMsSort);
        }
    }
}