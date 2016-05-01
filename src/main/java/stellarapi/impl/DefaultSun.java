package stellarapi.impl;

import stellarapi.api.CelestialPeriod;
import stellarapi.api.celestials.EnumCelestialObjectType;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.optics.Wavelength;

public class DefaultSun implements ICelestialObject {

	@Override
	public CelestialPeriod getAbsolutePeriod() {
		// No year in vanilla minecraft.
		return null;
	}

	@Override
	public CelestialPeriod getHorizontalPeriod() {
		return new CelestialPeriod("Day", 24000.0, 0.25);
	}

	@Override
	public CelestialPeriod getPhasePeriod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getCurrentPhase() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCurrentBrightness(Wavelength wavelength) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector3 getCurrentAbsolutePos() {
		return new Vector3(0.0, 0.0, 1.0);
	}

	@Override
	public SpCoord getCurrentHorizontalPos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getStandardMagnitude() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public EnumCelestialObjectType getObjectType() {
		return EnumCelestialObjectType.Star;
	}

}
