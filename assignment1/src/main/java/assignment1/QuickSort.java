package assignment1;

import assignment1.metrics.Metrics;
import assignment1.util.Utils;

public class QuickSort {

    public static void sort(int[] arr, Metrics metrics) {
        if (arr == null || arr.length <= 1) return;

        Utils.shuffle(arr);
        quickSort(arr, 0, arr.length - 1, metrics);
    }

    private static void quickSort(int[] arr, int low, int high, Metrics metrics) {
        metrics.enterRecursion();

        while (low < high) {
            int pivotIndex = Utils.partition(arr, low, high, metrics);


            if (pivotIndex - low < high - pivotIndex) {
                quickSort(arr, low, pivotIndex - 1, metrics);
                low = pivotIndex + 1;
            } else {
                quickSort(arr, pivotIndex + 1, high, metrics);
                high = pivotIndex - 1;
            }
        }

        metrics.exitRecursion();
    }
}
