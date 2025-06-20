package core;

public class Car {
    public Point2D position;

    public double heading;

    public double speed;

    public int currentSegment;

    public double segmentProgress;

    public Car() {
        this.position = new Point2D(0, 0);
        this.heading = 0;
        this.speed = 50;
        this.currentSegment = 0;
        this.segmentProgress = 0;
    }
}