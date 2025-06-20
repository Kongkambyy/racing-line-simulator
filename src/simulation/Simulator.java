package simulation;

import core.*;
import visualization.*;
import javax.swing.Timer;

public class Simulator {
    private RenderPanel renderPanel;
    private Timer animationTimer;
    private Car car;
    private Track track;

    private static final double TARGET_FPS = 500.0;
    private static final int TIMER_DELAY = (int)(1000.0 / TARGET_FPS);
    private static final double DT = 1.0 / TARGET_FPS;

    public Simulator(RenderPanel renderPanel) {
        this.renderPanel = renderPanel;
        this.track = renderPanel.getTrack();
        this.car = new Car();

        animationTimer = new Timer(TIMER_DELAY, e -> {
            updateSimulation();
            renderPanel.repaint();
        });
    }

    private void updateSimulation() {
        if (track.getCenterLine().size() < 2) {
            return;
        }

        double distanceToMove = car.speed * DT;
        moveCarAlongTrack(distanceToMove);
        updateCarWorldPosition();
    }

    private void moveCarAlongTrack(double distance) {
        Point2D segmentStart = track.getCenterLine().get(car.currentSegment);
        Point2D segmentEnd = track.getCenterLine().get(
                (car.currentSegment + 1) % track.getCenterLine().size()
        );

        double segmentLength = segmentStart.distanceTo(segmentEnd);
        double progressIncrement = distance / segmentLength;
        car.segmentProgress += progressIncrement;

        if (car.segmentProgress >= 1.0) {
            car.currentSegment = (car.currentSegment + 1) % track.getCenterLine().size();
            car.segmentProgress -= 1.0;
        }
    }

    private void updateCarWorldPosition() {
        Point2D segmentStart = track.getCenterLine().get(car.currentSegment);
        Point2D segmentEnd = track.getCenterLine().get(
                (car.currentSegment + 1) % track.getCenterLine().size()
        );

        double x = segmentStart.x + car.segmentProgress * (segmentEnd.x - segmentStart.x);
        double y = segmentStart.y + car.segmentProgress * (segmentEnd.y - segmentStart.y);

        car.position = new Point2D(x, y);

        double dx = segmentEnd.x - segmentStart.x;
        double dy = segmentEnd.y - segmentStart.y;
        car.heading = Math.atan2(dy, dx);
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
        car.currentSegment = 0;
        car.segmentProgress = 0;
        car.speed = 50;
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
}