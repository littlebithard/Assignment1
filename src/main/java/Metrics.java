import java.util.concurrent.atomic.LongAdder;

public class Metrics {
    public final String algorithmName;
    public long executionTimeNanos;
    private final LongAdder comparisons = new LongAdder();
    private int maxRecursionDepth = 0;
    private int currentDepth = 0;

    public Metrics(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void incrementComparisons() {
        comparisons.increment();
    }

    public void enterRecursion() {
        currentDepth++;
        if (currentDepth > maxRecursionDepth) {
            maxRecursionDepth = currentDepth;
        }
    }

    public void exitRecursion() {
        currentDepth--;
    }

    public void reset() {
        comparisons.reset();
        maxRecursionDepth = 0;
        currentDepth = 0;
        executionTimeNanos = 0;
    }

    @Override
    public String toString() {
        return String.format(
                "Algorithm: %s\n" +
                        "  - Execution Time: %.4f ms\n" +
                        "  - Comparisons: %,d\n" +
                        "  - Max Recursion Depth: %d",
                algorithmName,
                executionTimeNanos / 1_000_000.0,
                comparisons.longValue(),
                maxRecursionDepth
        );
    }
}