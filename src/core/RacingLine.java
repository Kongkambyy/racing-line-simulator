package core;

import java.util.ArrayList;
import java.util.List;

public class RacingLine {
    public List<Point2D> points;
    public List<Double> speeds;

    public RacingLine() {
        this.points = new ArrayList<>();
        this.speeds = new ArrayList<>();
    }

    public void clear() {
        points.clear();
        speeds.clear();
    }
}