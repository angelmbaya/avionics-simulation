import java.util.ArrayList;
import java.util.List;

public class RocketSimulator {

    public static final double G = 9.81; // m/s^2

    // Public so tests can access it
    public static class Derivatives {
        public final double dx;
        public final double dy;
        public final double dvx;
        public final double dvy;

        public Derivatives(double dx, double dy, double dvx, double dvy) {
            this.dx = dx;
            this.dy = dy;
            this.dvx = dvx;
            this.dvy = dvy;
        }
    }

    /**
     * Main simulation method.
     * Uses Heun's / improved Euler method.
     */
    public List<RocketState> simulate(RocketParameters params) {
        List<RocketState> traj = new ArrayList<>();

        RocketState state = new RocketState(0.0, 0.0, 0.0, 0.0, 0.0);
        traj.add(state);

        double dt = params.timeStep;
        double tMax = params.maxSimTime;

        while (state.t < tMax) {
            RocketState next = heunStep(state, params, dt);

            // Stop when rocket hits the ground after launch
            if (next.y <= 0.0 && next.t > 0.0) {
                RocketState grounded = interpolateToGround(state, next);
                traj.add(grounded);
                break;
            }

            traj.add(next);
            state = next;
        }

        return traj;
    }

    /**
     * One Heun / improved Euler step.
     */
    private RocketState heunStep(RocketState s, RocketParameters p, double dt) {
        Derivatives k1 = computeDerivatives(s, p);

        // Predictor
        RocketState sPredict = new RocketState(
                s.x + dt * k1.dx,
                s.y + dt * k1.dy,
                s.vx + dt * k1.dvx,
                s.vy + dt * k1.dvy,
                s.t + dt
        );

        Derivatives k2 = computeDerivatives(sPredict, p);

        double xNext  = s.x  + dt * 0.5 * (k1.dx  + k2.dx);
        double yNext  = s.y  + dt * 0.5 * (k1.dy  + k2.dy);
        double vxNext = s.vx + dt * 0.5 * (k1.dvx + k2.dvx);
        double vyNext = s.vy + dt * 0.5 * (k1.dvy + k2.dvy);
        double tNext  = s.t  + dt;

        return new RocketState(xNext, yNext, vxNext, vyNext, tNext);
    }

    /**
     * Public for JUnit tests.
     */
    public Derivatives computeDerivatives(RocketState s, RocketParameters p) {
        // Thrust (during burn)
        double axThrust = 0.0;
        double ayThrust = 0.0;

        if (s.t <= p.burnTime && p.thrustN > 0.0) {
            double angleRad = Math.toRadians(p.launchAngleDeg);
            double aMag = p.thrustN / p.massKg;
            axThrust = aMag * Math.cos(angleRad);
            ayThrust = aMag * Math.sin(angleRad);
        }

        // Drag
        double vx = s.vx;
        double vy = s.vy;
        double v = Math.sqrt(vx * vx + vy * vy);

        double axDrag = 0.0;
        double ayDrag = 0.0;

        if (v > 1e-6) {
            double dragMag = 0.5 * p.airDensity * p.dragCoeff *
                    p.crossSectionArea * v * v;
            double aDrag = dragMag / p.massKg;
            axDrag = -aDrag * (vx / v);
            ayDrag = -aDrag * (vy / v);
        }

        // Gravity
        double ayGravity = -G;

        double ax = axThrust + axDrag;
        double ay = ayThrust + ayDrag + ayGravity;

        double dxdt  = vx;
        double dydt  = vy;
        double dvxdt = ax;
        double dvydt = ay;

        return new Derivatives(dxdt, dydt, dvxdt, dvydt);
    }

    /**
     * Linear interpolation between two states to find when y crosses 0.
     */
    private RocketState interpolateToGround(RocketState s1, RocketState s2) {
        double y1 = s1.y;
        double y2 = s2.y;

        if (Math.abs(y1 - y2) < 1e-9) {
            return new RocketState(s2.x, 0.0, s2.vx, s2.vy, s2.t);
        }

        double alpha = y1 / (y1 - y2); // fraction of step to reach y=0
        alpha = Math.max(0.0, Math.min(1.0, alpha));

        double x  = s1.x  + alpha * (s2.x  - s1.x);
        double vx = s1.vx + alpha * (s2.vx - s1.vx);
        double vy = s1.vy + alpha * (s2.vy - s1.vy);
        double t  = s1.t  + alpha * (s2.t  - s1.t);

        return new RocketState(x, 0.0, vx, vy, t);
    }
}
