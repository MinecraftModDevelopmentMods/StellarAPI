package stellarapi.impl.celestial;

import stellarapi.api.CelestialPeriod;
import stellarapi.api.celestials.EnumObjectType;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.optics.Wavelength;

public class DefaultSun implements ICelestialObject {
	public DefaultSun() { }

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
		return null;
	}

	@Override
	public double getCurrentPhase() {
		return 0;
	}

	@Override
	public double getCurrentBrightness(Wavelength wavelength) {
		return 1.0;
	}

	@Override
	public Vector3 getCurrentPos() {
		return new Vector3(1.0, 0.0, 0.0);
	}

	@Override
	public double getStandardMagnitude() {
		// For astronomical convention
		return -26.74;
	}

	@Override
	public EnumObjectType getObjectType() {
		return EnumObjectType.Star;
	}

	@Override
	public String getName() {
		return "Sun";
	}
}
