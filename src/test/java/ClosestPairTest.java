import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClosestPairTest {

    private final Random random = new Random();

    private ClosestPair.Pair bruteForceClosestPair(ClosestPair.Point[] points) {
        if (points.length < 2) return null;
        ClosestPair.Pair minPair = new ClosestPair.Pair(points[0], points[1]);
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double dist = distance(points[i], points[j]);
                if (dist < minPair.distance) {
                    minPair.update(points[i], points[j], dist);
                }
            }
        }
        return minPair;
    }

    private double distance(ClosestPair.Point p1, ClosestPair.Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    @Test
    @DisplayName("Validate against O(n^2) brute-force on small n (<= 2000)")
    void testAgainstBruteForceImplementation() {
        int numPoints = 2000;
        ClosestPair.Point[] points = new ClosestPair.Point[numPoints];
        for (int i = 0; i < numPoints; i++) {
            points[i] = new ClosestPair.Point(random.nextDouble() * 1000, random.nextDouble() * 1000);
        }

        ClosestPair.Pair expectedPair = bruteForceClosestPair(points);
        ClosestPair.Pair actualPair = ClosestPair.findClosestPair(points, new Metrics("Test"));

        assertEquals(expectedPair.distance, actualPair.distance, 1e-9);
    }

    @Test
    @DisplayName("Test with a large n to ensure it runs without error")
    void testLargeN() {
        int numPoints = 50_000;
        ClosestPair.Point[] points = new ClosestPair.Point[numPoints];
        for (int i = 0; i < numPoints; i++) {
            points[i] = new ClosestPair.Point(random.nextDouble() * 10000, random.nextDouble() * 10000);
        }

        ClosestPair.Pair result = ClosestPair.findClosestPair(points, new Metrics("Test"));
        assertTrue(result.distance >= 0);
        assertTrue(result.p1 != null && result.p2 != null);
    }
}

