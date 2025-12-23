import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TrajectoryPanel extends JPanel {

    private List<RocketState> trajectory;

    public void setTrajectory(List<RocketState> trajectory) {
        this.trajectory = trajectory;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (trajectory == null || trajectory.size() < 2) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2f));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        double maxX = 0.0;
        double maxY = 0.0;
        for (RocketState s : trajectory) {
            maxX = Math.max(maxX, s.x);
            maxY = Math.max(maxY, s.y);
        }

        if (maxX < 1e-6) maxX = 1.0;
        if (maxY < 1e-6) maxY = 1.0;

        int w = getWidth();
        int h = getHeight();
        int margin = 40;

        double scaleX = (w - 2.0 * margin) / maxX;
        double scaleY = (h - 2.0 * margin) / maxY;

        // Axes
        g2.drawLine(margin, h - margin, w - margin, h - margin); // x-axis
        g2.drawLine(margin, h - margin, margin, margin);         // y-axis

        g2.drawString("x (m)", w - margin + 5, h - margin + 5);
        g2.drawString("y (m)", margin - 25, margin - 10);

        // Trajectory
        for (int i = 0; i < trajectory.size() - 1; i++) {
            RocketState s1 = trajectory.get(i);
            RocketState s2 = trajectory.get(i + 1);

            int x1 = (int) (margin + s1.x * scaleX);
            int y1 = (int) (h - margin - s1.y * scaleY);
            int x2 = (int) (margin + s2.x * scaleX);
            int y2 = (int) (h - margin - s2.y * scaleY);

            g2.drawLine(x1, y1, x2, y2);
        }
    }
}
