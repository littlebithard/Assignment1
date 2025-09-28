package org.assignment.closest;

import org.assignment.util.Metrics;
import java.util.Arrays;
import java.util.Comparator;

public class ClosestPair {
    public static class Point { public final double x,y; public Point(double x,double y){this.x=x;this.y=y;} }

    public static double closest(Point[] pts) {
        if (pts == null || pts.length < 2) return Double.POSITIVE_INFINITY;
        Point[] byX = pts.clone();
        Arrays.sort(byX, Comparator.comparingDouble(p->p.x));
        Point[] byY = pts.clone();
        Arrays.sort(byY, Comparator.comparingDouble(p->p.y));
        return rec(byX, byY, 0, pts.length);
    }

    private static double dist(Point a, Point b) {
        double dx = a.x-b.x, dy = a.y-b.y; return Math.hypot(dx,dy);
    }

    private static double rec(Point[] byX, Point[] byY, int lo, int hi) {
        Metrics.enter();
        try {
            int n = hi - lo;
            if (n <= 3) {
                double best = Double.POSITIVE_INFINITY;
                for (int i = lo; i < hi; i++) for (int j = i+1; j < hi; j++) best = Math.min(best, dist(byX[i], byX[j]));
                return best;
            }
            int mid = (lo+hi)/2;
            double midx = byX[mid].x;

            Point[] leftY = new Point[mid-lo];
            Point[] rightY = new Point[hi-mid];
            int li=0, ri=0;
            for (Point p: byY) {
                if (p.x <= midx && li < leftY.length) leftY[li++] = p;
                else rightY[ri++] = p;
            }
            double dl = rec(byX, leftY, lo, mid);
            double dr = rec(byX, rightY, mid, hi);
            double d = Math.min(dl, dr);

            Point[] strip = new Point[byY.length];
            int s=0;
            for (Point p: byY) if (Math.abs(p.x - midx) < d) strip[s++] = p;
            for (int i=0;i<s;i++){
                for (int j=i+1;j<s && j<i+8;j++){
                    d = Math.min(d, dist(strip[i], strip[j]));
                }
            }
            return d;
        } finally {
            Metrics.exit();
        }
    }
}
