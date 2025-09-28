package org.assignment.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

public class Metrics {
    public static final ThreadLocal<AtomicInteger> depth = ThreadLocal.withInitial(AtomicInteger::new);
    public static final ThreadLocal<AtomicInteger> comparisons = ThreadLocal.withInitial(AtomicInteger::new);

    public static void enter() { depth.get().incrementAndGet(); }
    public static void exit() { depth.get().decrementAndGet(); }
    public static int getDepth() { return depth.get().get(); }

    public static void clear() {
        depth.get().set(0);
        comparisons.get().set(0);
    }

    public static void cmp() { comparisons.get().incrementAndGet(); }
    public static int getComparisons() { return comparisons.get().get(); }

    public static void writeCsvHeader(String path) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path, false))) {
            pw.println("algo,n,time_ms,max_depth,comparisons");
        }
    }
    public static void appendCsv(String path, String line) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path, true))) {
            pw.println(line);
        }
    }
}
