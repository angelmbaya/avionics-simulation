Overview
![Rocket trajectory simulation](assets/trajectory_demo.png)
This project is a 2D rocket launch and trajectory simulator built in Java, focused on modeling real-world physics and responsive software design. It simulates rocket motion under thrust, drag, and gravity using Heun’s (Improved Euler) method, and visualizes trajectories through a Swing-based GUI.
The simulation runs on a background thread to keep the interface responsive. After it finishes, the application plots the trajectory and displays flight time, maximum height, and horizontal range.


Features
- 2D simulation of rocket motion with thrust, drag, and gravity

- Numerical integration using Heun’s (Improved Euler) method

- Adjustable simulation parameters (mass, thrust, angle, drag coefficient, time step, etc.)

- Multithreaded simulation to keep the GUI responsive.

- Trajectory plotting with automatically scaled axes.
