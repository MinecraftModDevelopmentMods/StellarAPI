package stellarapi.impl.celestial;

import net.minecraft.world.World;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.ICelestialCoordinates;
import stellarapi.api.lib.math.Matrix3;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.lib.math.Spmath;
import stellarapi.api.lib.math.Vector3;

public class DefaultCoordinateVanilla implements ICelestialCoordinates {

	private World world;
	private Matrix3 projREqToHor = new Matrix3().setAsRotation(1.0, 0.0, 0.0, Math.PI / 2);

	public DefaultCoordinateVanilla(World world) {
		this.world = world;
	}

	// Ground directions are x : East, y : North=Pole, z : Zenith,
	// Absolute(Equatorial) direction are x : Sun, y : perpendicular, z : Pole
	// Note that on tick #0, celestial angle starts from -90.0
	@Override
	public Matrix3 getProjectionToGround() {
		return new Matrix3().set(this.projREqToHor).preMult(
				new Matrix3().setAsRotation(0.0, 0.0, 1.0, -Math.PI / 2 - 2 * Math.PI * world.getCelestialAngle(0.0f)));
	}

	@Override
	public CelestialPeriod getPeriod() {
		return new CelestialPeriod("Celestial Day", 24000.0, 0.75);
	}

	@Override
	public double getHighestHeightAngle(Vector3 absPos) {
		SpCoord coord = new SpCoord().setWithVec(absPos);
		return Math.abs(90.0 - Math.abs(coord.y));
	}

	@Override
	public double getLowestHeightAngle(Vector3 absPos) {
		SpCoord coord = new SpCoord().setWithVec(absPos);
		return -Math.abs(90.0 - Math.abs(coord.y));
	}

	@Override
	public double calculateInitialOffset(Vector3 initialAbsPos, double periodLength) {
		SpCoord coord = new SpCoord().setWithVec(initialAbsPos);

		return (0.25 - coord.x / 360.0) % 1.0;
	}

	@Override
	public double offsetTillObjectReach(Vector3 absPos, double heightAngle) {
		SpCoord coord = new SpCoord().setWithVec(absPos);

		double maxHeight = Math.abs(90.0 - Math.abs(coord.y));
		if (heightAngle > maxHeight || heightAngle < -maxHeight)
			return Double.NaN;

		return Math.acos(-Spmath.sind(heightAngle) / (Spmath.cosd(coord.y))) / (2 * Math.PI);
	}
}
