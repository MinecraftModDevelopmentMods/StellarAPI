package stellarapi.impl.celestial;

import net.minecraft.world.World;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.celestials.EnumObjectType;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.optics.Wavelength;

public class DefaultMoon implements ICelestialObject {

	private World world;

	public DefaultMoon(World world) {
		this.world = world;
	}

	@Override
	public CelestialPeriod getAbsolutePeriod() {
		// Month is not absolute period in minecraft; It does not alter position of the moon.
		return null;
	}

	@Override
	public CelestialPeriod getHorizontalPeriod() {
		return new CelestialPeriod("Lunar Day", 24000.0, 0.75);
	}

	@Override
	public CelestialPeriod getPhasePeriod() {
		return new CelestialPeriod("Lunar Month", 24000.0 * 8.0, 0.5);
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
	public EnumObjectType getObjectType() {
		return EnumObjectType.Planet;
	}

	@Override
	public String getName() {
		return "Moon";
	}
}
