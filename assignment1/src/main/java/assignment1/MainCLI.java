package assignment1;

import java.util.Arrays;
import java.util.Random;

import assignment1.metrics.CSVWriter;
import assignment1.metrics.Metrics;

public class MainCLI {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java MainCLI <algorithm> <size>");
            System.out.println("Algorithms: merge, quick");
            return;
        }

        String algorithm = args[0].toLowerCase();
        int size = Integer.parseInt(args[1]);


        Random random = new Random(42);
        int[] arr = random.ints(size, -100_000, 100_000).toArray();
        int[] expected = Arrays.copyOf(arr, arr.length);
        Arrays.sort(expected);

        Metrics metrics = new Metrics();

        double start, end;

        switch (algorithm) {
            case "merge":
                start = System.nanoTime();
                MergeSort.sort(arr, metrics);
                end = System.nanoTime();
                break;
            case "quick":
                start = System.nanoTime();
                QuickSort.sort(arr, metrics);
                end = System.nanoTime();
                break;
            default:
                System.out.println("Unknown algorithm: " + algorithm);
                return;
        }

        if (!Arrays.equals(arr, expected)) {
            System.err.println("Sorting failed!");
            return;
        }

        double time_ns = (end - start) / 1000000.0;

        CSVWriter csv = new CSVWriter("metrics.csv");
        csv.writeMetrics(algorithm, size, metrics, time_ns);

        System.out.printf("%sSort(%d) => Comparisons: %d, Allocations: %d, MaxDepth: %d%n",
                algorithm, size,
                metrics.comparisons,
                metrics.allocations,
                metrics.maxDepth);
    }
}