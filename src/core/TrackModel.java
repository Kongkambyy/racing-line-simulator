package core;

public class TrackModel {

    private final TireModel tiremodel;
    private final Track track;

    public TrackModel (TireModel tiremodel, Track track) {
        this.tiremodel = tiremodel;
        this.track = track;
    }

    public double[] calculateResultingForces(double slipAngle, double slipRatio, double normalLoad) {

        double currentSurfaceFriction = this.track.getFrictionCoefficient();
        double demandedLateralForce = tiremodel.calculateLateralForce(slipAngle, normalLoad);
        double demandedLongitudinalForce = tiremodel.calculateLongitudinalForce(slipRatio, normalLoad);
        double effectiveFriction = tiremodel.peakFrictionCoeff * currentSurfaceFriction;

        double maxForce = effectiveFriction * Math.pow(normalLoad, tiremodel.loadSensitivity);
        double demandedTotalForce = Math.hypot(demandedLateralForce, demandedLongitudinalForce);

        double[] finalForce = new double[2];

        if (maxForce < demandedTotalForce) {
            var scaleFactor = maxForce / demandedTotalForce;
            finalForce[0] = demandedLateralForce * scaleFactor;
            finalForce[1] = demandedLongitudinalForce * scaleFactor;
        } else {
            finalForce[0] = demandedLateralForce;
            finalForce[1] = demandedLongitudinalForce;
        }
        return finalForce;
    }
}
