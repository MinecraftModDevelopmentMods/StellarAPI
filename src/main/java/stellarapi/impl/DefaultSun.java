package stellarapi.impl;

import net.minecraft.world.World;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.optics.Wavelength;

public class DefaultSun implements ICelestialObject {

	private World world;

	public DefaultSun(World world) {
		this.world = world;
	}

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
	public Vector3 getCurrentAbsolutePos() {
		return new Vector3(1.0, 0.0, 0.0);
	}

	@Override
	public SpCoord getCurrentHorizontalPos() {
		float celestialAngle = world.getCelestialAngle(0.0f);
		return new SpCoord(celestialAngle < 0.5 ? 0.0 : 180.0, 360.0 * Math.abs(celestialAngle - 0.5) - 90.0);
	}

	@Override
	public double getStandardMagnitude() {
		// For astronomical convention
		return -26.74;
	}

	@Override
	public EnumCelestialCategory getObjectType() {
		return EnumCelestialCategory.Star;
	}

	@Override
	public String getName() {
		return "Sun";
	}

	/*
	 * @Override public ImmutableList<String> additionalNumericalProperties() {
	 * return ImmutableList.of(); }
	 * 
	 * @Override public double additionalNumericalProperty(String propertyName)
	 * { return 0; }
	 * 
	 * @Override public ImmutableList<String> additionalGenericProperties() {
	 * return ImmutableList.of(); }
	 * 
	 * @Override public String additionalGenericProperty(String propertyName) {
	 * return null; }
	 */

}
