package stellarapi.feature.celestial.tweakable;

import stellarapi.api.CelestialPeriod;
import stellarapi.api.celestials.EnumObjectType;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.optics.Wavelength;

public class SAPISun implements ICelestialObject {
	private double dayLength;
	private double offset;

	public SAPISun(double day, double dayOffset) {
		this.dayLength = day;
		this.offset = dayOffset / day;
	}

	@Override
	public CelestialPeriod getAbsolutePeriod() {
		// No year in vanilla minecraft.
		return null;
	}

	@Override
	public CelestialPeriod getHorizontalPeriod() {
		// Fake it, refraction is hard here
		return new CelestialPeriod("Day", this.dayLength, this.offset);
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
