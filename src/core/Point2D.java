package core;

public class Point2D {
    public double x, y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Point2D other) {
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}