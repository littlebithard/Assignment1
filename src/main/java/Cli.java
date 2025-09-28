import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Cli {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            printUsage();
            return;
        }

        String algorithm = args[0].toLowerCase();
        int n;
        try {
            n = Integer.parseInt(args[1]);
            if (n <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.err.println("Error: <input_size> must be a positive integer.");
            printUsage();
            return;
        }

        String outputFile = (args.length == 3) ? args[2] : null;

        Metrics metrics = new Metrics(algorithm);

        long startTime = System.nanoTime();
        switch (algorithm) {
            case "mergesort":
                runMergeSort(n, metrics);
                break;
            case "quicksort":
                runQuickSort(n, metrics);
                break;
            case "select":
                runDeterministicSelect(n, metrics);
                break;
            case "closestpair":
                runClosestPair(n, metrics);
                break;
            default:
                System.err.println("Error: Unknown algorithm '" + algorithm + "'");
                printUsage();
                return;
        }
        long endTime = System.nanoTime();
        metrics.executionTimeNanos = endTime - startTime;

        String csvLine = formatAsCsv(metrics, n);

        if (outputFile != null) {
            writeCsvToFile(outputFile, csvLine);
        } else {
            System.out.println("Algorithm,InputSize,ExecutionTime_ms,Comparisons,MaxRecursionDepth");
            System.out.println(csvLine);
        }
    }

    private static void runMergeSort(int n, Metrics metrics) {
        int[] arr = generateRandomArray(n, n * 2);
        MergeSort.sort(arr, metrics);
    }

    private static void runQuickSort(int n, Metrics metrics) {
        int[] arr = generateRandomArray(n, n * 2);
        QuickSort.sort(arr, metrics);
    }

    private static void runDeterministicSelect(int n, Metrics metrics) {
        int[] arr = generateRandomArray(n, n * 2);
        int k = n / 2; // Select the median
        Select.select(arr, k, metrics);
    }

    private static void runClosestPair(int n, Metrics metrics) {
        if (n < 2) {
            System.err.println("Error: ClosestPair requires at least 2 points.");
            System.exit(1);
        }
        ClosestPair.Point[] points = generateRandomPoints(n, n);
        ClosestPair.findClosestPair(points, metrics);
    }

    private static String formatAsCsv(Metrics metrics, int n) {
        return String.format("%s,%d,%.4f,%d,%d",
                metrics.algorithmName,
                n,
                metrics.executionTimeNanos / 1_000_000.0
        );
    }

    private static void writeCsvToFile(String filename, String csvLine) {
        try (FileWriter fw = new FileWriter(filename, true); // 'true' for appending
             PrintWriter pw = new PrintWriter(fw)) {

            // If the file is new, write the header first
            if (new java.io.File(filename).length() == 0) {
                pw.println("Algorithm,InputSize,ExecutionTime_ms,Comparisons,MaxRecursionDepth");
            }
            pw.println(csvLine);
            System.out.println("Successfully wrote metrics to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to file '" + filename + "': " + e.getMessage());
        }
    }

    private static void printUsage() {
        System.out.println("Usage: java Cli <algorithm> <input_size> [output_file.csv]");
        System.out.println("  Algorithms: mergesort, quicksort, select, closestpair");
        System.out.println("  Example: java Cli mergesort 10000 results.csv");
    }

    private static int[] generateRandomArray(int size, int bound) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = RANDOM.nextInt(bound);
        }
        return arr;
    }

    private static ClosestPair.Point[] generateRandomPoints(int numPoints, int bound) {
        ClosestPair.Point[] points = new ClosestPair.Point[numPoints];
        for (int i = 0; i < numPoints; i++) {
            points[i] = new ClosestPair.Point(RANDOM.nextDouble() * bound, RANDOM.nextDouble() * bound);
        }
        return points;
    }
}
