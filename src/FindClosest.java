

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class FindClosest {
    public static Point2D.Double[] arr1;
    private PointPair closestPointPair;
    private final QuickSort quicksort = new QuickSort();

    /**
     * Constructor
     *
     * @param points --> point array
     */
    public FindClosest(Point2D.Double[] points) {
        //Sort points by X coordinate
        arr1 = points;
        quicksort.sort(points, 0, points.length - 1, "compareX");
        this.closestPointPair = calculateClosestPointPair(points, 0, points.length - 1);
        //*********************************do nothing***************************************//
    }

    /**
     * Get closest Point Pair
     *
     * @return closestPointPair
     */
    public PointPair getClosestPointPair() {
        return this.closestPointPair;
    }

    /**
     * Main method for calculate and return closest point pair
     *
     * @param p          --> point array
     * @param startIndex --> First index of p[]
     * @param lastIndex  --> last index of p[]
     * @return
     */
    private PointPair calculateClosestPointPair(Point2D.Double[] p, int startIndex, int lastIndex) {

        if ((lastIndex - startIndex) <= 3) {
            return this.getClosestPointPair(p[1], p[2], p[3]);
        }

        int mid = (startIndex + lastIndex) / 2;
        Point2D.Double midPoint = p[mid];

        PointPair dl = calculateClosestPointPair(p, startIndex, mid);
        PointPair dr = calculateClosestPointPair(p, mid, lastIndex);

        double distance = getClosestPointPair(dl, dr).getDistance();

        ArrayList<Point2D.Double> strip = new ArrayList<Point2D.Double>();

        for (int i = 0; i < lastIndex; i++) {
            if (Math.abs(p[i].getX() - midPoint.getX()) < distance) {
                strip.add(p[i]);
            }
        }

        Point2D.Double[] stripArray = strip.toArray(new Point2D.Double[0]);
        PointPair minInStrip = stripClosest(stripArray, stripArray.length, getClosestPointPair(dl, dr));

        if (minInStrip.getDistance() < getClosestPointPair(dr, dl).getDistance()) {
            return minInStrip;
        }
        return getClosestPointPair(dr, dl);
    }

    /**
     * calculate and return closest point pair from 3 points
     *
     * @param p1 --> point 1
     * @param p2 --> point 2
     * @param p3 --> point 3
     * @return
     */
    private PointPair getClosestPointPair(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
        double distanceBetweenP1andP2 = p1.distance(p2);
        double distanceBetweenP1andP3 = p1.distance(p3);
        double distanceBetweenP2andP2 = p2.distance(p3);

        if (distanceBetweenP1andP2 < distanceBetweenP1andP3) {
            if (distanceBetweenP1andP2 < distanceBetweenP2andP2) {
                return new PointPair(p1, p2);
            }
            return new PointPair(p2, p3);
        }

        if (distanceBetweenP1andP3 < distanceBetweenP2andP2) {
            return new PointPair(p1, p3);
        }

        return new PointPair(p2, p3);
    }

    private PointPair getClosestPointPair(PointPair p1, PointPair p2) {
        return (p1.getDistance() < p2.getDistance()) ? p1 : p2;
    }

    /**
     * A utility function to find the distance between the closest points of
     * strip of given size. All points in strip[] are sorted according to
     * y coordinate. They all have an upper bound on minimum distance as d.
     * Note that this method seems to be a O(n^2) method, but it's a O(n)
     * method as the inner loop runs at most 6 times
     *
     * @param strip        --> point array
     * @param size         --> strip array element count
     * @param shortestLine --> shortest line calculated so far
     * @return --> new shortest line
     */
    private PointPair stripClosest(Point2D.Double strip[], int size, PointPair shortestLine) {

        PointPair min = shortestLine;
        quicksort.sort(strip, 0, strip.length - 1, "compareY");

        for (int i = 0; i < size; ++i) {
            for (int j = i + 1; j < size && (strip[j].y - strip[i].y) < min.getDistance(); ++j) {
                PointPair temp = new PointPair(strip[i], strip[j]);
                if (temp.getDistance() < min.getDistance()) {
                    min = temp;
                }
            }
        }
        return min;

    }

}