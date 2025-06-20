package visualization;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private RenderPanel renderPanel;

    public MainWindow() {
        setTitle("Racing Line Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        renderPanel = new RenderPanel();
        add(renderPanel, BorderLayout.CENTER);

        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public RenderPanel getRenderPanel() {
        return renderPanel;
    }
}