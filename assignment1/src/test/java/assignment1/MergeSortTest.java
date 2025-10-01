package assignment1;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;

import assignment1.metrics.Metrics;

public class MergeSortTest {

    @Test
    void testEmptyArray() {
        int[] arr = {};
        Metrics metrics = new Metrics();
        MergeSort.sort(arr, metrics);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void testSingleElement() {
        int[] arr = {42};
        Metrics metrics = new Metrics();
        MergeSort.sort(arr, metrics);
        assertArrayEquals(new int[]{42}, arr);
    }

    @Test
    void testSortedArray() {
        int[] arr = {1, 2, 3, 4, 5};
        Metrics metrics = new Metrics();
        MergeSort.sort(arr, metrics);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);


    }

    @Test
    void testReversedArray() {
        int[] arr = {5, 4, 3, 2, 1};
        Metrics metrics = new Metrics();
        MergeSort.sort(arr, metrics);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);


    }

    @Test
    void testRandomArray() {
        Random random = new Random(42);
        int[] arr = random.ints(100_000, -100_000, 100_000).toArray();

        int[] expected = Arrays.copyOf(arr, arr.length);
        Arrays.sort(expected);

        Metrics metrics = new Metrics();
        MergeSort.sort(arr, metrics);
        assertArrayEquals(expected, arr);

    }


    
}
