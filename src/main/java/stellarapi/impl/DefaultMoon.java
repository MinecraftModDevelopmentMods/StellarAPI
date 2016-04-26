package stellarapi.impl;

import stellarapi.api.CelestialPeriod;
import stellarapi.api.celestials.EnumCelestialObjectType;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.optics.Wavelength;

public class DefaultMoon implements ICelestialObject {

	@Override
	public CelestialPeriod getAbsolutePeriod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CelestialPeriod getHorizontalPeriod() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

}
