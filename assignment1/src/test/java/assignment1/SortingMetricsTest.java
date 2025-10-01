package assignment1;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;

import assignment1.metrics.CSVWriter;
import assignment1.metrics.Metrics;

public class SortingMetricsTest {

    private static final int[] SIZES = {100, 1000, 10000};
    private static final String CSV_FILE = "metrics.csv";

    @Test
    void testSortingAlgorithmsMetrics() {
        CSVWriter writer = new CSVWriter(CSV_FILE);


        Random random = new Random(42);

        for (int size : SIZES) {
            int[] arr = random.ints(size, -100_000, 100_000).toArray();

            // MergeSort
            int[] mergeArr = Arrays.copyOf(arr, arr.length);
            Metrics mergeMetrics = new Metrics();


            long start = System.nanoTime();
            MergeSort.sort(mergeArr, mergeMetrics);
            long end = System.nanoTime();

            double time_ms = (end - start) / 1000000.0;

            int[] expected = Arrays.copyOf(arr, arr.length);
            Arrays.sort(expected);
            assertArrayEquals(expected, mergeArr);


            writer.writeMetrics("MergeSort", size, mergeMetrics, time_ms);

            // QuickSort
            int[] quickArr = Arrays.copyOf(arr, arr.length);
            Metrics quickMetrics = new Metrics();

            start = System.nanoTime();
            QuickSort.sort(quickArr, quickMetrics);
            end = System.nanoTime();

            time_ms = (end - start) / 1000000.0;

            assertArrayEquals(expected, quickArr);


            writer.writeMetrics("QuickSort", size, quickMetrics, time_ms);
        }
    }
}
