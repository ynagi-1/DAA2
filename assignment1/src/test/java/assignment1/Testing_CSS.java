package assignment1;

import java.util.Arrays;
import java.util.Random;

import assignment1.ClosestPair.Point;
import assignment1.metrics.Metrics;

// Testing
// Sorting: Check correctness on random and adversarial arrays. Verify recursion depth bounded: `QS depth ≤ ~2*floor(log₂ n) + O(1)`.
// Select: Compare results with `Arrays.sort(a)[k]` for 100 random trials.
// Closest Pair: Validate against brute-force O(n²) for small n (≤ 2000). Use optimized algorithm for large n.


public class Testing_CSS {

    
    public static void main(String[] args) {
        System.out.println("=== Sorting Tests ===");
        testSorting(QuickSort::sort, "QuickSort");
        testSorting(MergeSort::sort, "MergeSort");

        System.out.println("\n=== Deterministic Select Tests ===");
        testSelect(100, 1000);

        System.out.println("\n=== Closest Pair Tests ===");
        testClosestPair(2000);
        testClosestPair(10000);
    }

    interface SortFunction { void sort(int[] arr, Metrics m); }

    private static void testSorting(SortFunction sortFunc, String name) {
        Random rnd = new Random(42);
        int[] arr = new int[1000];
        for (int i = 0; i < arr.length; i++) arr[i] = rnd.nextInt(10_000);

        int[] arrCopy = arr.clone();
        Metrics metrics = new Metrics();

        sortFunc.sort(arr, metrics);
        Arrays.sort(arrCopy);

        System.out.println(name + " correct: " + Arrays.equals(arr, arrCopy));
        System.out.println(name + " recursion depth: " + metrics.maxDepth +
                           ", expected bound ~ " + (2 * (int)(Math.floor(Math.log(arr.length)/Math.log(2))) + 5));
    }

    private static void testSelect(int trials, int n) {
        Random rnd = new Random(42);

        for (int t = 0; t < trials; t++) {
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) arr[i] = rnd.nextInt(10_000);

            int k = rnd.nextInt(n);
            int[] expected = arr.clone();
            Arrays.sort(expected);

            Metrics metrics = new Metrics();
            int selectResult = DeterministicSelect.select(arr.clone(), k, metrics);

            if (selectResult != expected[k]) {
                System.out.println("Select failed at trial " + t + ": k=" + k +
                                   ", got " + selectResult + ", expected " + expected[k]);
            }
        }
        System.out.println("DeterministicSelect tests finished for " + trials + " trials.");
    }

    private static void testClosestPair(int n) {
        Random rnd = new Random(42);
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++)
            points[i] = new Point(rnd.nextDouble()*1000, rnd.nextDouble()*1000);

        if (n <= 2000) {
            double minDistBF = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) {
                for (int j = i+1; j < n; j++) {
                    double dx = points[i].x - points[j].x;
                    double dy = points[i].y - points[j].y;
                    minDistBF = Math.min(minDistBF, Math.sqrt(dx*dx + dy*dy));
                }
            }

            Metrics metrics = new Metrics();
            double minDistFast = ClosestPair.closestPair(points, metrics);

            System.out.println("n=" + n + " | Brute-force: " + minDistBF +
                               ", Fast: " + minDistFast +
                               ", Diff: " + Math.abs(minDistBF - minDistFast));
        } else {
            Metrics metrics = new Metrics();
            double minDistFast = ClosestPair.closestPair(points, metrics);
            System.out.println("n=" + n + " | Fast closest pair distance: " + minDistFast +
                               ", Recursion depth: " + metrics.maxDepth);
        }
    }
}
