Overview
This project is a 2D rocket launch and trajectory simulator built in Java, focused on modeling real-world physics and responsive software design. It simulates rocket motion under thrust, drag, and gravity using Heun’s (Improved Euler) method, and visualizes trajectories through a Swing-based GUI.
The simulation runs on a background thread to maintain UI responsiveness, and includes a JUnit test suite to validate core physics behavior. This project demonstrates numerical modeling, multithreaded application design, and test-driven verification.


Features
- 2D simulation of rocket motion with thrust, drag, and gravity

- Numerical integration using Heun’s (Improved Euler) method

- Adjustable simulation parameters (mass, thrust, angle, drag coefficient, time step, etc.)

- Multithreaded simulation to keep the GUI responsive.

- Real-time trajectory plotting with auto-scaling axes.

- Physics validation through JUnit 5 testing (gravity-only free-fall case).


Testing
The test directory contains a JUnit 5 test suite (RocketSimulatorTest) that verifies the correctness of the physics model under controlled conditions.
Tests can be executed directly using IntelliJ’s built-in test runner.
