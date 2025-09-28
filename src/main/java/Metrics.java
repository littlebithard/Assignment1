public final class Metrics {
    public long comparisons;
    public long swaps;
    public long allocations;
    private int currentDepth = 0;
    public int maxDepth = 0;
    public long startTime;
    public long endTime;

    public void enterRecursion() {
        currentDepth++;
        if (currentDepth > maxDepth) maxDepth = currentDepth;
    }

    public void exitRecursion() {
        currentDepth--;
    }

    public void startTimer() { startTime = System.nanoTime(); }
    public void stopTimer() { endTime = System.nanoTime(); }
    public double elapsedMillis() { return (endTime - startTime) / 1_000_000.0; }
}