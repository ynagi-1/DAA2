package assignment1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import assignment1.metrics.Metrics;

public class ClosestPairTest {

    @Test
    public void testSimple() {
        ClosestPair.Point[] pts = {
                new ClosestPair.Point(0, 0),
                new ClosestPair.Point(3, 4),
                new ClosestPair.Point(1, 1)
        };
        Metrics metrics = new Metrics();
        double result = ClosestPair.closestPair(pts, metrics);
        assertEquals(Math.sqrt(2), result, 1e-6);

    }

    @Test
    public void testTwoPoints() {
        ClosestPair.Point[] pts = {
                new ClosestPair.Point(0, 0),
                new ClosestPair.Point(5, 0)
        };
        Metrics metrics = new Metrics();
        double result = ClosestPair.closestPair(pts, metrics);
        assertEquals(5.0, result, 1e-6);

    }

    @Test
    public void testLine() {
        ClosestPair.Point[] pts = {
                new ClosestPair.Point(0, 0),
                new ClosestPair.Point(1, 0),
                new ClosestPair.Point(2, 0),
                new ClosestPair.Point(3, 0)
        };
        Metrics metrics = new Metrics();
        double result = ClosestPair.closestPair(pts, metrics);
        assertEquals(1.0, result, 1e-6);

    }

    @Test
    public void testRandom() {
        ClosestPair.Point[] pts = {
                new ClosestPair.Point(2, 3),
                new ClosestPair.Point(12, 30),
                new ClosestPair.Point(40, 50),
                new ClosestPair.Point(5, 1),
                new ClosestPair.Point(12, 10),
                new ClosestPair.Point(3, 4)
        };
        Metrics metrics = new Metrics();
        double result = ClosestPair.closestPair(pts, metrics);
        assertEquals(Math.sqrt(2), result, 1e-6);

    }

    @Test
    public void testInvalidInput() {
        Metrics metrics = new Metrics();
        assertThrows(IllegalArgumentException.class,
                () -> ClosestPair.closestPair(new ClosestPair.Point[]{}, metrics));
    }
}
