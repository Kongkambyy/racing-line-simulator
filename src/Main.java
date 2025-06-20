import visualization.*;
import simulation.*;
import core.*;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            RenderPanel renderPanel = window.getRenderPanel();

            createSampleTrack(renderPanel.getTrack());

            Simulator simulator = new Simulator(renderPanel);

            renderPanel.setCar(simulator.getCar());

            simulator.start();

            System.out.println("Simulation started!");
            System.out.println("Track points: " + renderPanel.getTrack().centerLine.size());
        });
    }

    private static void createSampleTrack(Track track) {
        int points = 80;
        double radius = 200;

        for (int i = 0; i < points; i++) {
            double angle = (2 * Math.PI * i) / points;
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            track.addPoint(x, y);
        }

        System.out.println("Created track with " + points + " points");
    }
}