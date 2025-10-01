package assignment1.metrics;

public class Metrics {
    public long comparisons = 0;
    public long currentDepth = 0;
    public long maxDepth = 0;
    public long allocations = 0;

    public void incComparisons() {
        comparisons++;
    }

    public void enterRecursion() {
        currentDepth++;
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }
    }

    public void exitRecursion() {
        currentDepth--;
    }

    public void addAllocations(long n) {
        allocations += n;
    }
}