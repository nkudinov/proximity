package com.proximity.proximity.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Rectangle {
    double x;

    @Override
    public String toString() {
        return "x=" + x + ",y=" + y + ",width=" + width + ",height=" + height;
    }

    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    double y;
    double width;
    double height;
}

public class QuadTreeNode {
    private final static int MAX_CAPACITY = 4;

    public static final int TOTAL_X_DEGREES = 360; // -180 to 180 - longitude
    public static final int TOTAL_Y_DEGREES = 180; // -90 to 90   - latitude

    private static final int NORMALIZE_X = 180;
    private static final int NORMALIZE_Y = 90;
    private final Rectangle bounds;
    List<QuadTreeNode> children;
    private final List<Customer> points;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("bounds=").append(bounds).append(",");
        if (this.children.size() != 0) {
            for (QuadTreeNode next : this.children) {
                sb.append(next.toString());
            }
        }
        return sb.toString();
    }

    public QuadTreeNode() {
        this(0, 0, TOTAL_X_DEGREES, TOTAL_Y_DEGREES);
    }

    public int getPointsListSize() {
        return points.size();
    }

    public QuadTreeNode(double lat, double lon, double width, double height) {
        this.bounds = new Rectangle(lat, lon, width, height);
        this.points = new ArrayList<Customer>();
        this.children = new ArrayList<>();
    }

    private void split() {
        if (children.size() != 0) {
            return;
        }
        double x = bounds.x;
        double y = bounds.y;
        double halfHeight = bounds.height / 2.0;
        double halfWidth = bounds.width / 2.0;
        this.children.add(new QuadTreeNode(x, y, halfWidth, halfHeight)); //bottomLeft
        this.children.add(new QuadTreeNode(x + halfWidth, y, halfWidth, halfHeight));//bottomRight
        this.children.add(new QuadTreeNode(x, y + halfHeight, halfWidth, halfHeight));//topLeft
        this.children.add(new QuadTreeNode(x + halfWidth, y + halfHeight, halfWidth, halfHeight));//topRight
    }

    QuadTreeNode getTopLeft() {
        if (children.size() != 4) {
            return null;
        }
        return children.get(2);
    }

    QuadTreeNode getTopRight() {
        if (children.size() != 4) {
            return null;
        }
        return children.get(3);
    }

    private double normalizeLatitude(double latitude) {
        return latitude + NORMALIZE_X;
    }

    private double normalizeLongitude(double longitude) {
        return longitude + NORMALIZE_Y;
    }

    private boolean contains(double x, double y) {
        return x >= bounds.x && x <= bounds.x + bounds.width && y >= bounds.y && y <= bounds.y + bounds.height;
    }

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // convert latitude and longitude to radians
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        // calculate the differences between the coordinates
        double latDiff = lat2 - lat1;
        double lonDiff = lon2 - lon1;

        // apply the Haversine formula to calculate the distance
        double a = Math.pow(Math.sin(latDiff / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(lonDiff / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // return the distance in kilometers
        return 6371 * c;
    }

    public List<Customer> search(double lat, double lon, double r) {
        Queue<QuadTreeNode> q = new LinkedList<>();
        q.add(this);
        List<Customer> ans = new ArrayList<>();
        while (!q.isEmpty()) {
            QuadTreeNode curr = q.poll();
            for (Customer customer : curr.points) {
                if (calculateDistance(lat, lon, customer.lat, customer.lon) < r) {
                    ans.add(customer);
                }
            }
            double x = normalizeLatitude(lon);
            double y = normalizeLongitude(lat);
            for (QuadTreeNode next : curr.children) {
                if (next.contains(x, y)) {
                    q.add(next);
                }
            }
        }
        return ans;
    }

    public void add(Customer customer) {
        if (points.size() < MAX_CAPACITY) {
            points.add(customer);
        } else {
            split();
            double x = normalizeLatitude(customer.lon);
            double y = normalizeLongitude(customer.lat);
            boolean found = false;
            for (QuadTreeNode next : this.children) {
                if (next.contains(x, y)) {
                    next.add(customer);
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new RuntimeException("can not find a node for pair(" + x + "," + y + "). Current node is:" + this);
            }
        }
    }
}
