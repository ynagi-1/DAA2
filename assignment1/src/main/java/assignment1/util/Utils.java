package assignment1.util;

import java.util.Random;

import assignment1.metrics.Metrics;

public class Utils {
    private static final Random RANDOM = new Random();

    // Swapping elements
    public static void swap(int[] arr, int i, int j) {
        guardNotNull(arr);
        if (i == j) return;
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Fisher–Yates shuffle
    public static void shuffle(int[] arr) {
        guardNotNull(arr);
        for (int i = arr.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            swap(arr, i, j);
        }
    }

    // Elements less than pivot → left, bigger → right
    public static int partition(int[] arr, int low, int high, Metrics metrics) {
        guardNotNull(arr);
        guardRange(arr, low, high);

        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            metrics.incComparisons();
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    public static int partitionWithPivot(int[] arr, int left, int right, int pivot, Metrics metrics) {
        int pivotIndex = left;
        for (int i = left; i <= right; i++) {
            metrics.incComparisons();
            if (arr[i] == pivot) {
                pivotIndex = i;
                break;
            }
        }
        swap(arr, pivotIndex, right);

        int storeIndex = left;
        for (int i = left; i < right; i++) {
            metrics.incComparisons();
            if (arr[i] < pivot) {
                swap(arr, i, storeIndex);
                storeIndex++;
            }
        }
        swap(arr, storeIndex, right);
        return storeIndex;
    }

    // Checks if array is not null
    public static void guardNotNull(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
    }

    // Checks if low/high values are in the range
    public static void guardRange(int[] arr, int low, int high) {
        if (low < 0 || high >= arr.length || low > high) {
            throw new IllegalArgumentException("Invalid range: low=" + low + ", high=" + high);
        }
    }

    // Insertion Sort Algorithm
    public static void insertionSort(int[] a, int left, int right, Metrics metrics) {
        guardNotNull(a);
        guardRange(a, left, right);

        for (int i = left + 1; i <= right; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= left) {
                metrics.incComparisons(); 
                if (a[j] > key) {
                    a[j + 1] = a[j];
                    j--;
                } else {
                    break;
                }
            }
            a[j + 1] = key;
        }
    }
}
