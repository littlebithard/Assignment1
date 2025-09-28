import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ClosestPair {

    public static class Point {
        public final double x;
        public final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return String.format("(%.2f, %.2f)", x, y);
        }
    }

    public static class Pair {
        public Point p1;
        public Point p2;
        public double distance;

        public Pair(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = distance(p1, p2);
        }

        public void update(Point p1, Point p2, double dist) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = dist;
        }

        @Override
        public String toString() {
            return String.format("Closest pair: %s, %s, Distance: %.4f", p1, p2, distance);
        }
    }

    public static Pair findClosestPair(Point[] points, Metrics metrics) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("At least two points are required.");
        }

        Point[] pointsSortedByX = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsSortedByX, Comparator.comparingDouble(p -> p.x));

        Point[] pointsSortedByY = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsSortedByY, Comparator.comparingDouble(p -> p.y));

        return findClosestPairRecursive(pointsSortedByX, pointsSortedByY, 0, points.length - 1, metrics);
    }

    private static Pair findClosestPairRecursive(Point[] pointsByX, Point[] pointsByY, int left, int right, Metrics metrics) {
        metrics.enterRecursion();

        if (right - left <= 2) {
            metrics.exitRecursion();
            return bruteForce(pointsByX, left, right, metrics);
        }

        int mid = left + (right - left) / 2;
        Point midPoint = pointsByX[mid];

        Pair leftPair = findClosestPairRecursive(pointsByX, pointsByY, left, mid, metrics);
        Pair rightPair = findClosestPairRecursive(pointsByX, pointsByY, mid + 1, right, metrics);

        metrics.exitRecursion();

        Pair minPair;
        metrics.incrementComparisons();
        if (leftPair.distance < rightPair.distance) {
            minPair = leftPair;
        } else {
            minPair = rightPair;
        }

        double minDistance = minPair.distance;

        List<Point> strip = new ArrayList<>();
        for (Point p : pointsByY) {
            if (Math.abs(p.x - midPoint.x) < minDistance) {
                strip.add(p);
            }
        }

        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < minDistance; j++) {
                metrics.incrementComparisons();
                double dist = distance(strip.get(i), strip.get(j));
                metrics.incrementComparisons();
                if (dist < minPair.distance) {
                    minPair.update(strip.get(i), strip.get(j), dist);
                }
            }
        }

        return minPair;
    }

    private static Pair bruteForce(Point[] points, int left, int right, Metrics metrics) {
        if (right - left < 1) return new Pair(null, null) {{ distance = Double.POSITIVE_INFINITY; }};

        Pair minPair = new Pair(points[left], points[left + 1]);

        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                metrics.incrementComparisons();
                double dist = distance(points[i], points[j]);
                metrics.incrementComparisons();
                if (dist < minPair.distance) {
                    minPair.update(points[i], points[j], dist);
                }
            }
        }
        return minPair;
    }

    private static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}
