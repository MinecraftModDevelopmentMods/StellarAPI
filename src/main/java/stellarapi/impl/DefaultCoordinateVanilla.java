package stellarapi.impl;

import net.minecraft.world.World;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.lib.math.Matrix3;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.lib.math.Vector3;

public class DefaultCoordinateVanilla implements ICelestialCoordinate {
	
	private World world;
	
	public DefaultCoordinateVanilla(World world) {
		this.world = world;
	}

	//Ground direction is x : East, y : North, z : Zenith.
	//On tick #0, starts from -90.0
	@Override
	public Matrix3 getProjectionToGround() {
		return new Matrix3().setAsRotation(0.0, -1.0, 0.0, 2*Math.PI*world.getCelestialAngle(0.0f));
	}

	@Override
	public CelestialPeriod getPeriod() {
		return new CelestialPeriod("Day", 24000.0, 0.75);
	}

	@Override
	public double getHighestHeightAngle(Vector3 absPos) {
		SpCoord coord = new SpCoord().setWithVec(absPos);
		return Math.abs(90.0 - Math.abs(180.0-coord.x));
	}

	@Override
	public double getLowestHeightAngle(Vector3 absPos) {
		SpCoord coord = new SpCoord().setWithVec(absPos);
		return -Math.abs(90.0 - Math.abs(180.0-coord.x));
	}

	@Override
	public double calculateInitialOffset(Vector3 initialAbsPos, double periodLength) {
		SpCoord coord = new SpCoord().setWithVec(initialAbsPos);
		return coord.x < 180.0? 0.75 + coord.y / 360.0 : 0.25 - coord.y / 360.0;
	}

	@Override
	public double offsetTillObjectReach(Vector3 absPos, double heightAngle) {		
		SpCoord coord = new SpCoord().setWithVec(absPos);
		
		double maxHeight = -Math.abs(90.0 - Math.abs(180.0-coord.x));
		if(heightAngle > maxHeight || heightAngle < -maxHeight)
			return Double.NaN;
		
		//double absoulteOffset = heightAngle - maxHeight;
		double initialOffset = coord.x < 180.0? 0.75 + coord.y / 360.0 : 0.25 - coord.y / 360.0;
		
		// TODO Auto-generated method stub
		return 0;
	}

}
