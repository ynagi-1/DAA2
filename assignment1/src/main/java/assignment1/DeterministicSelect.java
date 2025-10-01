package assignment1;

import assignment1.metrics.Metrics;
import assignment1.util.Utils;

public class DeterministicSelect {

    public static int select(int[] arr, int k, Metrics metrics) {
        Utils.guardNotNull(arr);
        Utils.guardRange(arr, 0, k);

        metrics.enterRecursion();
        int result = select(arr, 0, arr.length - 1, k, metrics);
        metrics.exitRecursion();

        return result;
    }

    private static int select(int[] arr, int left, int right, int k, Metrics metrics) {
        metrics.enterRecursion();

        if (left == right) {
            metrics.exitRecursion();
            return arr[left];
        }

        int pivot = medianOfMedians(arr, left, right, metrics);
        int pivotIndex = Utils.partitionWithPivot(arr, left, right, pivot, metrics);

        metrics.incComparisons();
        if (k == pivotIndex) {
            metrics.exitRecursion();
            return arr[pivotIndex];
        }

        metrics.incComparisons();
        int res;
        if (k < pivotIndex) {
            res = select(arr, left, pivotIndex - 1, k, metrics);
        } else {
            res = select(arr, pivotIndex + 1, right, k, metrics);
        }

        metrics.exitRecursion();
        return res;
    }

    private static int medianOfMedians(int[] arr, int left, int right, Metrics metrics) {
        int n = right - left + 1;
        if (n < 5) {
            Utils.insertionSort(arr, left, right, metrics);
            return arr[left + n / 2];
        }

        int numMedians = (int) Math.ceil(n / 5.0);
        int[] medians = new int[numMedians];
        metrics.addAllocations(numMedians); 

        for (int i = 0; i < numMedians; i++) {
            int subLeft = left + i * 5;
            int subRight = Math.min(subLeft + 4, right);

            Utils.insertionSort(arr, subLeft, subRight, metrics);
            medians[i] = arr[subLeft + (subRight - subLeft) / 2];
        }

        return select(medians, 0, numMedians - 1, numMedians / 2, metrics);
    }
}
