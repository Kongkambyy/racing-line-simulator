package simulation;

import core.*;
import visualization.*;
import javax.swing.Timer;

public class Simulator {
    private RenderPanel renderPanel;
    private Timer animationTimer;
    private Car car;
    private Track track;

    private static final double DT = 0.016; //

    public Simulator(RenderPanel renderPanel) {
        this.renderPanel = renderPanel;
        this.track = renderPanel.getTrack();
        this.car = new Car();

        animationTimer = new Timer(16, e -> {
            System.out.println("Timer Tick!");
            updateSimulation();
            renderPanel.repaint();
        });
    }

    private void updateSimulation() {
        if (track.centerLine.size() < 2) {
            System.out.println("Track has less than 2 points!");
            return;
        }

        double distanceToMove = car.speed * DT;

        moveCarAlongTrack(distanceToMove);

        updateCarWorldPosition();
    }

    private void moveCarAlongTrack(double distance) {
        Point2D segmentStart = track.centerLine.get(car.currentSegment);
        Point2D segmentEnd = track.centerLine.get(
                (car.currentSegment + 1) % track.centerLine.size()
        );

        double segmentLength = segmentStart.distanceTo(segmentEnd);

        double progressIncrement = distance / segmentLength;

        car.segmentProgress += progressIncrement;

        if (car.segmentProgress >= 1.0) {
            car.currentSegment = (car.currentSegment + 1) % track.centerLine.size();
            car.segmentProgress -= 1.0;
        }
    }

    private void updateCarWorldPosition() {
        Point2D segmentStart = track.centerLine.get(car.currentSegment);
        Point2D segmentEnd = track.centerLine.get(
                (car.currentSegment + 1) % track.centerLine.size()
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
}