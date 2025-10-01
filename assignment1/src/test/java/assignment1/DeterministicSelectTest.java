package assignment1;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import assignment1.metrics.Metrics;

public class DeterministicSelectTest {

    @Test
    void testSingleElement() {
        int[] arr = {42};
        Metrics metrics = new Metrics();
        assertEquals(42, DeterministicSelect.select(arr, 0, metrics));

    }

    @Test
    void testSortedArray() {
        int[] arr = {1, 2, 3, 4, 5};
        Metrics metrics = new Metrics();
        assertEquals(1, DeterministicSelect.select(arr, 0, metrics));
        assertEquals(3, DeterministicSelect.select(arr, 2, metrics));
        assertEquals(5, DeterministicSelect.select(arr, 4, metrics));

    }

    @Test
    void testUnsortedArray() {
        int[] arr = {7, 2, 9, 4, 1, 5};
        Metrics metrics = new Metrics();
        assertEquals(1, DeterministicSelect.select(arr, 0, metrics));
        assertEquals(4, DeterministicSelect.select(arr, 2, metrics));
        assertEquals(9, DeterministicSelect.select(arr, 5, metrics));

    }

    @Test
    void testWithDuplicates() {
        Metrics metrics = new Metrics();
        assertEquals(1, DeterministicSelect.select(new int[]{5, 1, 5, 2, 5, 3}, 0, metrics));
        assertEquals(5, DeterministicSelect.select(new int[]{5, 1, 5, 2, 5, 3}, 3, metrics));
        assertEquals(5, DeterministicSelect.select(new int[]{5, 1, 5, 2, 5, 3}, 4, metrics));

    }

    @Test
    void testRandomArrayConsistency() {
        Random random = new Random(42);
        int[] arr = random.ints(100, 0, 1000).toArray();
        int[] sorted = arr.clone();
        Arrays.sort(sorted);

        Metrics metrics = new Metrics();
        for (int k : new int[]{0, 10, 50, 99}) {
            assertEquals(sorted[k], DeterministicSelect.select(arr.clone(), k, metrics));
        }

    }

    @Test
    void testInvalidK() {
        int[] arr = {1, 2, 3};
        Metrics metrics = new Metrics();
        assertThrows(IllegalArgumentException.class, () -> DeterministicSelect.select(arr, -1, metrics));
        assertThrows(IllegalArgumentException.class, () -> DeterministicSelect.select(arr, 3, metrics));
    }

    @Test
    void testEmptyArray() {
        int[] arr = {};
        Metrics metrics = new Metrics();
        assertThrows(IllegalArgumentException.class, () -> DeterministicSelect.select(arr, 0, metrics));
    }
}
