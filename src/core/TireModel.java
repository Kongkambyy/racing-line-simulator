package core;

public class TireModel {
    public double peakFrictionCoeff;
    public double peakSlipAngle;
    public double rollingResistanceCoeff;
    public double loadSensitivity;
    public double peakSlipRatio;

    public TireModel() {
        this.peakFrictionCoeff = 1.0;
        this.peakSlipAngle = Math.toRadians(8.0);
        this.rollingResistanceCoeff = 0.015;
        this.loadSensitivity = 0.8;
        this.peakSlipRatio = 0.15;
    }

    public double calculateLateralForce (double slipAngle, double normalLoad) {

        // slipAngle being how much the tire is turned compared to the direction the car is going.
        // peakSlipAngle is where the car has the most grip.
        double normalizedSlip = Math.abs(slipAngle) / peakSlipAngle;

        double forceFactor;
        if (normalizedSlip <= 1.0) {
            forceFactor = normalizedSlip * (2.0 - normalizedSlip);
        } else {
            forceFactor = 1.0 - 0.3 * (normalizedSlip - 1.0);
            if (forceFactor < 0.3) {
                forceFactor = 0.3;
            }
        }

        // Doubling the weight on a tire doesnt give you double the grip
        double maxForce = peakFrictionCoeff * Math.pow(normalLoad, loadSensitivity);

        return Math.signum(slipAngle) * maxForce * forceFactor;
    }

    public double calculateLongitudinalForce (double slipRatio, double normalLoad) {
        double normalizedSlip = Math.abs(slipRatio) / peakSlipRatio;

        double forceFactor;
        if (normalizedSlip <= 1.0) {
            forceFactor = normalizedSlip * (2.0 - normalizedSlip);
        } else {
            forceFactor = 1.0 - 0.15 * (normalizedSlip - 1.0);
            if (forceFactor < 0.15) {
                forceFactor = 0.15;
            }
        }

        double maxForce = peakFrictionCoeff * Math.pow(normalLoad, loadSensitivity);

        return Math.signum(slipRatio) * maxForce * forceFactor;
    }

    public double[] calculateCombinedForce(double lateralForce, double longitudinalForce, double normalLoad) {
        double maxTotalForce = peakFrictionCoeff * normalLoad;
        double demandedForce = Math.hypot(lateralForce, longitudinalForce);

        double[] resultingForces = new double[2];

        if (demandedForce <= maxTotalForce) {
            resultingForces[0] = lateralForce;
            resultingForces[1] = longitudinalForce;
        } else {
            double scaleFactor = maxTotalForce / demandedForce;
            resultingForces[0] = lateralForce * scaleFactor;
            resultingForces[1] = longitudinalForce * scaleFactor;
        }

        return resultingForces;
    }

    public double calculateRollingResistance(double normalLoad, double speed) {
        return rollingResistanceCoeff * normalLoad * (1.0 + speed * speed / 10000.0);
    }
}

