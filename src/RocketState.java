public class RocketState {
    public final double x;   // m
    public final double y;   // m
    public final double vx;  // m/s
    public final double vy;  // m/s
    public final double t;   // s

    public RocketState(double x, double y, double vx, double vy, double t) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.t = t;
    }
}
