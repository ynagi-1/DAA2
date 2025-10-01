package assignment1.bench;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import assignment1.DeterministicSelect;
import assignment1.MergeSort;
import assignment1.QuickSort;
import assignment1.metrics.CSVWriter;
import assignment1.metrics.Metrics;

/**
 * Improved harness for comparing DeterministicSelect,
 * and for measuring MergeSort / QuickSort behavior.
 * Notes:
 * - Default warmup/measurement are small to let you iterate quickly. For final runs
 *   override with CLI args: e.g. -wi 5 -i 5 -f 2
 * - To run a subset: add -p distribution=RANDOM -p n=100000 etc.
 */

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)

@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class SelectVsSortBenchmark {

    @Param({ "10", "100", "1000" })
    public int n;

    @Param({ "RANDOM", "SORTED", "REVERSE", "DUPLICATES", "ADVERSARIAL" })
    public String distribution;

    @Param({ "MIN", "MEDIAN", "MAX", "RANDOM" })
    public String kType;


    private int[] base;
    private Random rnd;


    private static final ConcurrentMap<String, AggregatedMetrics> AGG = new ConcurrentHashMap<>();


    private CSVWriter csv;

    @Setup(Level.Trial)
    public void setupTrial() {
        rnd = new Random(42);
        base = generateArray(n, distribution, rnd);
        AGG.clear();
        csv = new CSVWriter("/Users/nifflyar/vscode/DAA/daa_assignment1/assignment1/src/main/java/assignment1/bench/bench-results.csv");
    }

    @Setup(Level.Invocation)
    public void setupInvocation() {

    }

    private int[] fresh() {
        return Arrays.copyOf(base, base.length);
    }

    /* ------------- Sorting benchmarks -------------- */

    @Benchmark
    public int benchMergeSort() {
        int[] arr = fresh();
        Metrics m = new Metrics();
        long t0 = System.nanoTime();
        MergeSort.sort(arr, m);            
        long t = System.nanoTime() - t0;


        aggregate("mergesort", n, distribution, kType, m, t);

        int k = chooseK(kType, arr.length, rnd);
        return arr[k];
    }

    @Benchmark
    public int benchQuickSort() {
        int[] arr = fresh();
        Metrics m = new Metrics();
        long t0 = System.nanoTime();
        QuickSort.sort(arr, m);           
        long t = System.nanoTime() - t0;

        aggregate("quicksort", n, distribution, kType, m, t);
        int k = chooseK(kType, arr.length, rnd);
        return arr[k];
    }

    /* -------------- Selection benchmarks -------------- */

    @Benchmark
    public int benchDeterministicSelect() {
        int[] arr = fresh();
        Metrics m = new Metrics();
        int k = chooseK(kType, arr.length, rnd);

        long t0 = System.nanoTime();
        int res = DeterministicSelect.select(arr, k, m);
        long t = System.nanoTime() - t0;

        aggregate("select_mom", n, distribution, kType, m, t);
        return res;
    }

    @Benchmark
    public int benchSortAndPick() {
        int[] arr = fresh();
        Metrics m = new Metrics(); 
        int k = chooseK(kType, arr.length, rnd);

        long t0 = System.nanoTime();
        Arrays.sort(arr);
        long t = System.nanoTime() - t0;


        aggregate("sort_and_pick", n, distribution, kType, m, t);
        return arr[k];
    }

    /* ------------ Aggregation & TearDown ----------- */

    private void aggregate(String algorithm, int size, String distribution, String kType, Metrics m, long timeNs) {
        String key = algorithm + "|" + size + "|" + distribution + "|" + kType;
        AGG.compute(key, (k, old) -> {
            if (old == null) old = new AggregatedMetrics(algorithm, size, distribution, kType);
            old.add(m, timeNs);
            return old;
        });
    }

    @TearDown(Level.Trial)
    public void tearDownTrial() {

        for (AggregatedMetrics a : AGG.values()) {
            long runs = Math.max(1L, a.runs);
            double meanTimeNs = ((double) a.sumTimeNs) / runs;
            long meanComp = a.sumComparisons / runs;
            long meanAlloc = a.sumAllocations / runs;

            Metrics out = new Metrics();
            out.comparisons = meanComp;
            out.addAllocations(meanAlloc);
            out.maxDepth = a.maxDepth;


            String algLabel = a.algorithm + "|" + a.distribution + "|" + a.kType;
            csv.writeMetrics(algLabel, a.size, out, meanTimeNs);


            System.out.printf("Wrote CSV: alg=%s size=%d runs=%d meanTimeNs=%.0f meanComp=%d maxDepth=%d%n",
                    a.algorithm, a.size, a.runs, meanTimeNs, meanComp, a.maxDepth);
        }
        AGG.clear();
    }

    private static class AggregatedMetrics {
        final String algorithm;
        final int size;
        final String distribution;
        final String kType;

        long runs = 0;
        long sumTimeNs = 0;
        long sumComparisons = 0;
        long sumAllocations = 0;
        long maxDepth = 0;

        AggregatedMetrics(String algorithm, int size, String distribution, String kType) {
            this.algorithm = algorithm;
            this.size = size;
            this.distribution = distribution;
            this.kType = kType;
        }

        void add(Metrics m, long timeNs) {
            runs++;
            sumTimeNs += timeNs;
            sumComparisons += m.comparisons;
            sumAllocations += m.allocations;
            if (m.maxDepth > maxDepth) maxDepth = m.maxDepth;
        }
    }

    /* --------------- Utils -------------- */

    private int chooseK(String kType, int len, Random rnd) {
        switch (kType) {
            case "MIN": return 0;
            case "MAX": return Math.max(0, len - 1);
            case "MEDIAN": return len / 2;
            default: return rnd.nextInt(len);
        }
    }

    private int[] generateArray(int n, String distribution, Random rnd) {
        int[] a = new int[n];
        switch (distribution) {
            case "RANDOM":
                for (int i = 0; i < n; i++) a[i] = rnd.nextInt();
                break;
            case "SORTED":
                for (int i = 0; i < n; i++) a[i] = i;
                break;
            case "REVERSE":
                for (int i = 0; i < n; i++) a[i] = n - i;
                break;
            case "DUPLICATES":
                for (int i = 0; i < n; i++) a[i] = rnd.nextInt(10);
                break;
            case "ADVERSARIAL":

                for (int i = 0; i < n; i++) a[i] = (i % 2 == 0) ? i : n - i;
                break;
            default:
                for (int i = 0; i < n; i++) a[i] = rnd.nextInt();
        }
        return a;
    }
}
