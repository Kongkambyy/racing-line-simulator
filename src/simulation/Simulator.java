package simulation;

import core.*;
import visualization.*;
import javax.swing.Timer;

public class Simulator {
    private RenderPanel renderPanel;
    private Timer animationTimer;
    private Car car;
    private Track track;

    private static final double TARGET_FPS = 60.0;
    private static final int TIMER_DELAY = (int)(1000.0 / TARGET_FPS);
    private static final double DT = 1.0 / TARGET_FPS;
    private final TrackModel trackModel;

    public Simulator(RenderPanel renderPanel) {
        this.renderPanel = renderPanel;
        this.track = renderPanel.getTrack();
        this.car = new Car();

        car.velocity = new Vector2D(50, 0);

        TireModel defaultTire = new TireModel();
        this.trackModel = new TrackModel(defaultTire, this.track);

        animationTimer = new Timer(TIMER_DELAY, e -> {
            updateSimulation();
            renderPanel.repaint();
        });
    }

    private void updateSimulation() {
        if (track.getCenterLine().isEmpty()) {
            return;
        }

        int targetIndex = (car.targetSegment + 1) % track.getCenterLine().size();
        Point2D targetPoint = track.getCenterLine().get(targetIndex);

        double vectorToTargetX = targetPoint.x - car.position.x;
        double vectorToTargetY = targetPoint.y - car.position.y;

        double desiredHeading = Math.atan2(vectorToTargetY, vectorToTargetX);

        double headingError = normalizeAngle(desiredHeading - car.heading);

        double maxSteerAngle = Math.toRadians(45.0);
        car.steeringAngle = Math.max(-maxSteerAngle, Math.min(maxSteerAngle, headingError * 2.0));

        double distanceToTarget = car.position.distanceTo(targetPoint);
        if (distanceToTarget < 50.0) {
            car.targetSegment = targetIndex;
        }

        final double GRAVITY = 9.81;

        double normalLoadFrontAxle = (car.mass * GRAVITY) * 0.5;
        double normalLoadPerFrontTire = normalLoadFrontAxle / 2.0;

        double vehicleHeading = Math.atan2(car.velocity.y, car.velocity.x);
        double slipAngle = car.steeringAngle - normalizeAngle(vehicleHeading - car.heading);

        double[] resultingForces = trackModel.calculateResultingForces(slipAngle, 0.0, normalLoadPerFrontTire);

        double lateralForce = resultingForces[0];
        double worldForceX = lateralForce * -Math.sin(car.heading);
        double worldForceY = lateralForce * Math.cos(car.heading);

        double totalForceX = worldForceX * 2;
        double totalForceY = worldForceY * 2;

        double accelerationX = totalForceX / car.mass;
        double accelerationY = totalForceY / car.mass;

        car.velocity.x += accelerationX * DT;
        car.velocity.y += accelerationY * DT;
        car.position.x += car.velocity.x * DT;
        car.position.y += car.velocity.y * DT;
        car.heading = Math.atan2(car.velocity.y, car.velocity.x);

        if (car.position.distanceTo(new Point2D(0, 0)) > 1000) {
            car.position = new Point2D(0, 0);
            car.velocity = new Vector2D(50, 0);
            car.targetSegment = 0;
        }
    }

    public Car getCar() {
        return car;
    }

    public void start() {
        animationTimer.start();
    }

    public void stop() {
        animationTimer.stop();
    }

    public void reset() {
        stop();
        car.position = new Point2D(0, 0);
        car.velocity = new Vector2D(50, 0);
        car.targetSegment = 0;
        car.heading = 0;
    }

    public void setTargetFPS(double fps) {
        boolean wasRunning = animationTimer.isRunning();
        if (wasRunning) {
            stop();
        }

        int newDelay = (int)(1000.0 / fps);
        animationTimer.setDelay(newDelay);

        if (wasRunning) {
            start();
        }
    }

    private double normalizeAngle(double angle) {
        while (angle > Math.PI) angle -= 2 * Math.PI;
        while (angle < -Math.PI) angle += 2 * Math.PI;
        return angle;
    }
}