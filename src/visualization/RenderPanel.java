package visualization;

import core.*;
import core.Point2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.geom.AffineTransform;

public class RenderPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    private Track track;
    private RacingLine racingLine;
    private Camera camera;
    private Car car;

    private boolean showTrack = true;
    private boolean showRacingLine = true;
    private boolean showGrid = true;

    public RenderPanel() {
        setBackground(new Color(30, 30, 30));
        setDoubleBuffered(true);

        track = new Track();
        racingLine = new RacingLine();
        camera = new Camera();

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform originalTransform = g2d.getTransform();

        g2d.translate(getWidth() / 2, getHeight() / 2);
        g2d.scale(camera.zoom, camera.zoom);
        g2d.translate(-camera.x, -camera.y);

        if (showGrid) drawGrid(g2d);
        if (showTrack) drawTrack(g2d);
        if (showRacingLine) drawRacingLine(g2d);

        if (car != null) {
            System.out.println("Car is printed!");
            drawCar(g2d);
        } else {
            System.out.println("Car is null!");
        }

        g2d.setTransform(originalTransform);
    }

    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(new Color(50, 50, 50));
        g2d.setStroke(new BasicStroke(1.0f / (float)camera.zoom));

        int gridSize = 50;
        int gridCount = 20;

        for (int i = -gridCount; i <= gridCount; i++) {
            g2d.drawLine(i * gridSize, -gridCount * gridSize,
                    i * gridSize, gridCount * gridSize);
            g2d.drawLine(-gridCount * gridSize, i * gridSize,
                    gridCount * gridSize, i * gridSize);
        }
    }

    private void drawTrack(Graphics2D g2d) {
        if (track.centerLine.size() < 2) return;

        g2d.setStroke(new BasicStroke((float)track.trackWidth));
        g2d.setColor(new Color(80, 80, 80));

        Path2D path = new Path2D.Double();
        Point2D first = track.centerLine.get(0);
        path.moveTo(first.x, first.y);

        for (int i = 1; i < track.centerLine.size(); i++) {
            Point2D p = track.centerLine.get(i);
            path.lineTo(p.x, p.y);
        }

        if (track.centerLine.size() > 2) {
            path.closePath();
        }

        g2d.draw(path);

        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f,
                new float[]{10.0f, 10.0f}, 0.0f));
        g2d.setColor(Color.WHITE);
        g2d.draw(path);
    }

    private void drawRacingLine(Graphics2D g2d) {
        if (racingLine.points.size() < 2) return;

        g2d.setStroke(new BasicStroke(3.0f));

        for (int i = 0; i < racingLine.points.size() - 1; i++) {
            Point2D p1 = racingLine.points.get(i);
            Point2D p2 = racingLine.points.get(i + 1);

            double speed = racingLine.speeds.isEmpty() ? 0.5 :
                    racingLine.speeds.get(i);

            float hue = (float)(0.3 * speed);
            g2d.setColor(Color.getHSBColor(hue, 1.0f, 1.0f));

            g2d.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
        }
    }

    public void setTrack(Track track) {
        this.track = track;
        repaint();
    }

    public void setRacingLine(RacingLine racingLine) {
        this.racingLine = racingLine;
        repaint();
    }

    public Track getTrack() {
        return track;
    }

    public RacingLine getRacingLine() {
        return racingLine;
    }

    public void toggleTrack() {
        showTrack = !showTrack;
        repaint();
    }

    public void toggleRacingLine() {
        showRacingLine = !showRacingLine;
        repaint();
    }

    public void toggleGrid() {
        showGrid = !showGrid;
        repaint();
    }

    private Point lastMousePos;

    @Override
    public void mousePressed(MouseEvent e) {
        lastMousePos = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (lastMousePos != null) {
            int dx = e.getX() - lastMousePos.x;
            int dy = e.getY() - lastMousePos.y;

            camera.x -= dx / camera.zoom;
            camera.y -= dy / camera.zoom;

            lastMousePos = e.getPoint();
            repaint();
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double factor = e.getWheelRotation() < 0 ? 1.1 : 0.9;
        camera.zoom *= factor;
        camera.zoom = Math.max(0.1, Math.min(5.0, camera.zoom));
        repaint();
    }

    private void drawCar(Graphics2D g2d) {
        AffineTransform oldTransform = g2d.getTransform();

        g2d.translate(car.position.x, car.position.y);
        g2d.rotate(car.heading);

        g2d.setColor(Color.RED);
        g2d.fillRect(-15, -8, 30, 16);

        g2d.setColor(Color.YELLOW);
        g2d.fillPolygon(new int[]{15, 25, 15}, new int[]{-5, 0, 5}, 3);

        g2d.setTransform(oldTransform);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) { lastMousePos = null; }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}

    public void setCar(Car car) {
        this.car = car;
    }
}