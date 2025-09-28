package org.assignment.tests;

import org.assignment.closest.ClosestPair;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClosestTest {
    @Test
    public void testSmallBruteForce() {
        ClosestPair.Point[] pts = new ClosestPair.Point[] {
            new ClosestPair.Point(0,0),
            new ClosestPair.Point(1,0),
            new ClosestPair.Point(0,2),
            new ClosestPair.Point(0.1,0.1)
        };
        double d = ClosestPair.closest(pts);
        assertTrue(d > 0);
        assertEquals(Math.hypot(0.1,0.1), d, 1e-6);
    }
}
