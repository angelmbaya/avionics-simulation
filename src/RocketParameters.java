public class RocketParameters {

    public double massKg;              // kg
    public double thrustN;             // N
    public double launchAngleDeg;      // degrees from horizontal
    public double dragCoeff;           // dimensionless Cd
    public double crossSectionArea;    // m^2
    public double airDensity;          // kg/m^3
    public double burnTime;            // s
    public double timeStep;            // s
    public double maxSimTime;          // s

    public RocketParameters() {
        // Reasonable defaults
        this.massKg = 50.0;
        this.thrustN = 1500.0;
        this.launchAngleDeg = 75.0;
        this.dragCoeff = 0.5;
        this.crossSectionArea = 0.03;
        this.airDensity = 1.225;
        this.burnTime = 5.0;
        this.timeStep = 0.01;
        this.maxSimTime = 60.0;
    }
}
