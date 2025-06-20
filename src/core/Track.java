package core;

import java.util.ArrayList;
import java.util.List;

public class Track {
    private List<Point2D> centerLine;
    private double trackWidth;
    private double frictionCoefficient;

    public Track() {
        this.centerLine = new ArrayList<>();
        this.trackWidth = 50.0;
        this.frictionCoefficient = 0.9;
    }

    public void addPoint(double x, double y) {
        centerLine.add(new Point2D(x, y));
    }

    public void clear() {
        centerLine.clear();
    }

    public double getFrictionCoefficient() {
        return frictionCoefficient;
    }

    public void setFrictionCoefficient(double frictionCoefficient) {
        this.frictionCoefficient = frictionCoefficient;
    }

    public List<Point2D> getCenterLine() {
        return centerLine;
    }

    public double getTrackWidth() {
        return trackWidth;
    }

    public void setTrackWidth(double trackWidth) {
        this.trackWidth = trackWidth;
    }
}