package assignment1;

import java.util.Arrays;
import java.util.Comparator;

import assignment1.metrics.Metrics;

public class ClosestPair {

    public static class Point {
        double x, y;
        public Point(double x, double y) {
            this.x = x; this.y = y;
        }
    }

    public static double closestPair(Point[] points, Metrics metrics) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("Need at least 2 points");
        }

        metrics.addAllocations(points.length); 
        Point[] pts = points.clone();


        Arrays.sort(pts, Comparator.comparingDouble(p -> p.x));

        metrics.addAllocations(points.length); 
        Point[] aux = pts.clone();

        return closest(pts, aux, 0, pts.length - 1, metrics);
    }

    private static double closest(Point[] pts, Point[] aux, int lo, int hi, Metrics metrics) {
        metrics.enterRecursion();

        if (hi - lo <= 3) {
            double res = bruteForce(pts, lo, hi, metrics);
            metrics.exitRecursion();
            return res;
        }

        int mid = (lo + hi) / 2;
        double midX = pts[mid].x;

        double d1 = closest(pts, aux, lo, mid, metrics);
        double d2 = closest(pts, aux, mid + 1, hi, metrics);
        double d = Math.min(d1, d2);

        int m = 0;
        for (int i = lo; i <= hi; i++) {
            metrics.incComparisons(); 
            if (Math.abs(pts[i].x - midX) < d) {
                aux[m++] = pts[i];
            }
        }


        Arrays.sort(aux, 0, m, Comparator.comparingDouble(p -> p.y));

        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                metrics.incComparisons(); 
                if ((aux[j].y - aux[i].y) < d) {
                    d = Math.min(d, dist(aux[i], aux[j], metrics));
                } else {
                    break;
                }
            }
        }

        metrics.exitRecursion();
        return d;
    }

    private static double bruteForce(Point[] pts, int lo, int hi, Metrics metrics) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = lo; i <= hi; i++) {
            for (int j = i + 1; j <= hi; j++) {
                metrics.incComparisons(); 
                min = Math.min(min, dist(pts[i], pts[j], metrics));
            }
        }
        return min;
    }

    private static double dist(Point a, Point b, Metrics metrics) {
        metrics.incComparisons(); 
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
