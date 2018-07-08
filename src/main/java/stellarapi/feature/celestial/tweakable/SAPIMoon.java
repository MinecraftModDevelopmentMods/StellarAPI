package stellarapi.feature.celestial.tweakable;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.celestials.EnumObjectType;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.optics.Wavelength;

public class SAPIMoon extends CelestialObject {
	private World world;

	public SAPIMoon(World world, double day, double month, double dayOffset, double monthOffset) {
		super(new ResourceLocation("moon"), EnumObjectType.Planet);
		this.world = world;
		double relOffsetDay = dayOffset / day;
		double relOffsetMonth = monthOffset / month;

		this.setHoritontalPeriod(new CelestialPeriod("Lunar Day", day,
				relOffsetDay < 0.5? relOffsetDay + 0.5 : relOffsetDay - 0.5));
		this.setPhasePeriod(new CelestialPeriod("Lunar Month", day * month, relOffsetMonth));
		this.setPos(new Vector3(-1.0, 0.0, 0.0));
		this.setStandardMagnitude(-12.74);
	}

	@Override
	public double getCurrentPhase() {
		long worldTime = world.getWorldTime();
		int phase = (int) Math.floor(this.getPhasePeriod().getBiasedOffset(worldTime, 0.0f, 0.5) * 8.0);
		return WorldProvider.MOON_PHASE_FACTORS[phase];
	}

	@Override
	public double getCurrentBrightness(Wavelength wavelength) {
		return this.getCurrentPhase();
	}
}
