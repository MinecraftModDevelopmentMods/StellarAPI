package stellarapi.impl.celestial;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.celestials.EnumObjectType;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.optics.Wavelength;

public class DefaultMoon extends CelestialObject {
	private World world;

	public DefaultMoon(World world) {
		super(new ResourceLocation("moon"), EnumObjectType.Planet);
		this.world = world;

		this.setHoritontalPeriod(new CelestialPeriod("Lunar Day", 24000.0, 0.75));
		this.setPhasePeriod(new CelestialPeriod("Lunar Month", 24000.0 * 8.0, 0.5));
		this.setPos(new Vector3(-1.0, 0.0, 0.0));
		this.setStandardMagnitude(-12.74);
	}

	@Override
	public double getCurrentPhase() {
		return world.getCurrentMoonPhaseFactor();
	}

	@Override
	public double getCurrentBrightness(Wavelength wavelength) {
		return this.getCurrentPhase();
	}
}
