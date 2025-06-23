package core;

public class Car {
    public Point2D position;
    public double heading;
    public double speed;
    public int currentSegment;
    public double segmentProgress;
    public double mass;
    public Vector2D velocity;
    public double angularVelocity;

    public double steeringAngle;
    public double throttle;
    public double brake;
    public int targetSegment;

    public Car() {
        this.position = new Point2D(0, 0);
        this.heading = 0;
        this.speed = 50;
        this.currentSegment = 0;
        this.segmentProgress = 0;
        this.mass = 1200;
        this.velocity = new Vector2D(0, 0);
        this.angularVelocity = 0;
        this.steeringAngle = 0;
        this.throttle = 0;
        this.brake = 0;
        this.targetSegment = 0;

    }
}