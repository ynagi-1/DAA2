package assignment1.bench;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;

import assignment1.ClosestPair;
import assignment1.ClosestPair.Point;
import assignment1.metrics.Metrics;

public class ClosestPairSimpleBenchmark {

    public static void main(String[] args) throws Exception {

        int[] sizes = {10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000};
        int repeats = 5; 
        String csvPath = "/Users/nifflyar/vscode/DAA/daa_assignment1/assignment1/src/main/java/assignment1/bench/closest-results.csv";

        try (PrintWriter pw = new PrintWriter(new FileWriter(csvPath))) {
            pw.println("size,time_ms,comp,alloc,maxdepth");

            Random rnd = new Random(42);

            for (int n : sizes) {
                for (int r = 0; r < repeats; r++) {

                    Point[] points = new Point[n];
                    for (int i = 0; i < n; i++) {
                        points[i] = new Point(rnd.nextDouble() * 1000, rnd.nextDouble() * 1000);
                    }

                    Metrics metrics = new Metrics();
                    long start = System.nanoTime();
                    double minDist = ClosestPair.closestPair(points, metrics);
                    long elapsed = System.nanoTime() - start;

                    double timeMs = elapsed / 1_000_000.0;

                    pw.printf("%d,%.6f,%d,%d,%d%n",
                            n, timeMs, metrics.comparisons, metrics.allocations, metrics.maxDepth);

                    System.out.printf("n=%d repeat=%d -> time=%.3f ms, comp=%d, alloc=%d, depth=%d, minDist=%.3f%n",
                            n, r + 1, timeMs, metrics.comparisons, metrics.allocations, metrics.maxDepth, minDist);
                }
            }
        }

        System.out.println("Benchmark finished. Results saved to " + csvPath);
    }
}
