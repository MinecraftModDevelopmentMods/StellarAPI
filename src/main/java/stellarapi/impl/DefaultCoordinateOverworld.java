package stellarapi.impl;

import stellarapi.api.CelestialPeriod;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.lib.math.Matrix3;
import stellarapi.api.lib.math.Vector3;

public class DefaultCoordinateOverworld implements ICelestialCoordinate {

	@Override
	public Matrix3 getProjectionToGround() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CelestialPeriod getPeriod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getHighestHeightAngle(Vector3 absPos) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getLowestHeightAngle(Vector3 absPos) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double calculateInitialOffset(Vector3 initialAbsPos, double periodLength) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double offsetTillObjectReach(Vector3 absPos, double heightAngle) {
		// TODO Auto-generated method stub
		return 0;
	}

}
