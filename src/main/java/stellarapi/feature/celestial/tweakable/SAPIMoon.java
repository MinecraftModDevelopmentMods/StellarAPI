package stellarapi.feature.celestial.tweakable;

import net.minecraft.world.World;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.celestials.EnumCelestialObjectType;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.optics.Wavelength;

public class SAPIMoon implements ICelestialObject {
	private World world;
	private double dayLength;
	private double monthInDay;

	public SAPIMoon(World world, double day, double month) {
		this.world = world;
		this.dayLength = day;
		this.monthInDay = month;
	}

	@Override
	public CelestialPeriod getAbsolutePeriod() {
		// Month is not absolute period in minecraft; It does not alter position of the moon.
		return null;
	}

	@Override
	public CelestialPeriod getHorizontalPeriod() {
		return new CelestialPeriod("Lunar Day", this.dayLength, 0.75);
	}

	@Override
	public CelestialPeriod getPhasePeriod() {
		return new CelestialPeriod("Lunar Month", this.dayLength * this.monthInDay, 0.5);
	}

	@Override
	public double getCurrentPhase() {
		return world.getCurrentMoonPhaseFactor();
	}

	@Override
	public double getCurrentBrightness(Wavelength wavelength) {
		return this.getCurrentPhase();
	}

	@Override
	public Vector3 getCurrentAbsolutePos() {
		return new Vector3(-1.0, 0.0, 0.0);
	}

	@Override
	public SpCoord getCurrentHorizontalPos() {
		float celestialAngle = world.getCelestialAngle(0.0f);
		return new SpCoord(celestialAngle < 0.5 ? 180.0 : 0.0, 90.0 - 360.0 * Math.abs(celestialAngle - 0.5));
	}

	@Override
	public double getStandardMagnitude() {
		// For astronomical convention
		return -12.74;
	}

	@Override
	public EnumCelestialObjectType getObjectType() {
		return EnumCelestialObjectType.Planet;
	}

	@Override
	public String getName() {
		return "Moon";
	}
}
