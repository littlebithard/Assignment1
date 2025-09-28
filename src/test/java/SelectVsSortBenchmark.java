import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class SelectVsSortBenchmark {

    @Param({"1000", "10000", "100000"})
    private int N;

    private int[] sourceArray;
    private int k;

    @Setup
    public void setup() {
        sourceArray = new Random().ints(N, 0, N * 10).toArray();
        k = N / 2; // We will always find the median element.
    }

    @Benchmark
    public int deterministicSelect() {
        // We must clone the array because the select algorithm modifies it in-place.
        // Failing to do this would mean subsequent runs work on already-partitioned data, giving invalid results.
        int[] workArray = Arrays.copyOf(sourceArray, sourceArray.length);
        return DeterministicSelect.select(workArray, k, new Metrics("JMH_Select"));
    }

    @Benchmark
    public int sortAndSelect() {
        // We also clone here to ensure a fair comparison against the other benchmark.
        int[] workArray = Arrays.copyOf(sourceArray, sourceArray.length);
        Arrays.sort(workArray);
        return workArray[k];
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(SelectVsSortBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}

