package org.assignment.cli;

import org.assignment.sorts.Sorts;
import org.assignment.closest.ClosestPair;
import org.assignment.util.Metrics;

import java.io.IOException;
import java.util.Random;

public class Main {
    // args: n repeats out.csv
    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.out.println("Usage: java -jar ... <n> <repeats> <out.csv>");
            return;
        }
        int n = Integer.parseInt(args[0]);
        int repeats = Integer.parseInt(args[1]);
        String out = args[2];
        Metrics.writeCsvHeader(out);
        Random r = new Random(42);
        for (int rep=0; rep<repeats; rep++) {
            int[] a = r.ints(n, 0, Math.max(1, n*5)).toArray();
            Metrics.clear();
            long t0 = System.currentTimeMillis();
            Sorts.mergeSort(a.clone()); long t1 = System.currentTimeMillis();
            Metrics.appendCsv(out, String.format("mergesort,%d,%d,%d,%d", n, t1-t0, Metrics.getDepth(), Metrics.getComparisons()));
            Metrics.clear();
            t0 = System.currentTimeMillis();
            Sorts.quickSort(a.clone()); t1 = System.currentTimeMillis();
            Metrics.appendCsv(out, String.format("quicksort,%d,%d,%d,%d", n, t1-t0, Metrics.getDepth(), Metrics.getComparisons()));
            Metrics.clear();
            t0 = System.currentTimeMillis();
            int kth = Sorts.deterministicSelect(a.clone(), Math.max(0, n/2)); t1 = System.currentTimeMillis();
            Metrics.appendCsv(out, String.format("select,%d,%d,%d,%d", n, t1-t0, Metrics.getDepth(), Metrics.getComparisons()));
            Metrics.clear();
            ClosestPair.Point[] pts = new ClosestPair.Point[n];
            for (int i=0;i<n;i++) pts[i]=new ClosestPair.Point(r.nextDouble()*n, r.nextDouble()*n);
            t0 = System.currentTimeMillis();
            double d = ClosestPair.closest(pts);
            t1 = System.currentTimeMillis();
            Metrics.appendCsv(out, String.format("closest,%d,%d,%d,%d", n, t1-t0, Metrics.getDepth(), Metrics.getComparisons()));
        }
        System.out.println("Done -- wrote " + out);
    }
}
