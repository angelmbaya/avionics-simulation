import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class RocketSimulatorApp extends JFrame {

    private final JTextField massField      = new JTextField("50.0", 8);
    private final JTextField thrustField    = new JTextField("1500.0", 8);
    private final JTextField angleField     = new JTextField("75.0", 8);
    private final JTextField dragField      = new JTextField("0.5", 8);
    private final JTextField areaField      = new JTextField("0.03", 8);
    private final JTextField rhoField       = new JTextField("1.225", 8);
    private final JTextField burnField      = new JTextField("5.0", 8);
    private final JTextField dtField        = new JTextField("0.01", 8);
    private final JTextField maxTimeField   = new JTextField("60.0", 8);

    private final JLabel infoLabel          = new JLabel("Ready.");
    private final TrajectoryPanel trajPanel = new TrajectoryPanel();
    private final JButton simulateButton    = new JButton("Simulate");

    public RocketSimulatorApp() {
        super("Rocket Launch & Trajectory Simulator");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildControlPanel(), BorderLayout.NORTH);
        add(trajPanel, BorderLayout.CENTER);
        add(infoLabel, BorderLayout.SOUTH);

        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    private JPanel buildControlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 4, 2, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addRow(panel, gbc, row++, "Mass (kg):", massField);
        addRow(panel, gbc, row++, "Thrust (N):", thrustField);
        addRow(panel, gbc, row++, "Angle (deg):", angleField);
        addRow(panel, gbc, row++, "Drag Cd:", dragField);
        addRow(panel, gbc, row++, "Area (m²):", areaField);
        addRow(panel, gbc, row++, "Air density (kg/m³):", rhoField);
        addRow(panel, gbc, row++, "Burn time (s):", burnField);
        addRow(panel, gbc, row++, "dt (s):", dtField);
        addRow(panel, gbc, row++, "Max time (s):", maxTimeField);

        simulateButton.addActionListener(this::onSimulate);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(simulateButton, gbc);

        return panel;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row,
                        String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    /**
     * Uses a background thread to run the simulation.
     */
    private void onSimulate(ActionEvent e) {
        try {
            RocketParameters p = new RocketParameters();
            p.massKg           = Double.parseDouble(massField.getText());
            p.thrustN          = Double.parseDouble(thrustField.getText());
            p.launchAngleDeg   = Double.parseDouble(angleField.getText());
            p.dragCoeff        = Double.parseDouble(dragField.getText());
            p.crossSectionArea = Double.parseDouble(areaField.getText());
            p.airDensity       = Double.parseDouble(rhoField.getText());
            p.burnTime         = Double.parseDouble(burnField.getText());
            p.timeStep         = Double.parseDouble(dtField.getText());
            p.maxSimTime       = Double.parseDouble(maxTimeField.getText());

            infoLabel.setText("Running simulation...");
            simulateButton.setEnabled(false);

            // 🔹 BACKGROUND THREAD
            Thread worker = new Thread(() -> {
                RocketSimulator simulator = new RocketSimulator();
                List<RocketState> trajectory = simulator.simulate(p);

                // Compute stats off the EDT
                double maxHeight = 0.0;
                double maxRange  = 0.0;
                double flightTime = 0.0;
                for (RocketState s : trajectory) {
                    maxHeight  = Math.max(maxHeight, s.y);
                    maxRange   = Math.max(maxRange, s.x);
                    flightTime = s.t;
                }

                // 🔹 Update GUI on the EDT
                double finalFlightTime = flightTime;
                double finalMaxHeight = maxHeight;
                double finalMaxRange = maxRange;
                SwingUtilities.invokeLater(() -> {
                    trajPanel.setTrajectory(trajectory);
                    infoLabel.setText(String.format(
                            "Flight time: %.2f s | Max height: %.2f m | Range: %.2f m",
                            finalFlightTime, finalMaxHeight, finalMaxRange
                    ));
                    simulateButton.setEnabled(true);
                });
            });

            worker.start();   // actually starts the new thread

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric values.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RocketSimulatorApp app = new RocketSimulatorApp();
            app.setVisible(true);
        });
    }
}
