package core;

import java.util.ArrayList;
import java.util.List;

public class Track {
    public List<Point2D> centerLine;
    public double trackWidth;

    public Track() {
        this.centerLine = new ArrayList<>();
        this.trackWidth = 50.0;
    }

    public void addPoint(double x, double y) {
        centerLine.add(new Point2D(x, y));
    }

    public void clear() {
        centerLine.clear();
    }
}