package stellarapi.feature.celestial.tweakable;

import net.minecraft.world.World;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.celestials.EnumObjectType;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.optics.Wavelength;

public class SAPISun implements ICelestialObject {
	private World world;
	private double dayLength;
	private double offset;

	public SAPISun(World world, double day, double dayOffset) {
		this.world = world;
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
	public EnumObjectType getObjectType() {
		return EnumObjectType.Star;
	}

	@Override
	public String getName() {
		return "Sun";
	}
}
