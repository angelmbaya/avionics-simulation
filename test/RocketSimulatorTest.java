import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RocketSimulatorTest {

    @Test
    public void testFreeFallAccelerationNoDrag() {
        RocketParameters p = new RocketParameters();
        p.thrustN    = 0.0;   // no thrust
        p.dragCoeff  = 0.0;   // no drag

        RocketSimulator sim = new RocketSimulator();
        RocketState s = new RocketState(0.0, 100.0, 0.0, 0.0, 0.0);

        RocketSimulator.Derivatives d = sim.computeDerivatives(s, p);

        assertEquals(0.0, d.dx, 1e-9);
        assertEquals(0.0, d.dy, 1e-9);
        assertEquals(0.0, d.dvx, 1e-9);
        assertEquals(-RocketSimulator.G, d.dvy, 1e-6);
    }
}
