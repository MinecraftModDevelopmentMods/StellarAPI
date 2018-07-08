package stellarapi.feature.celestial.tweakable;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.celestials.EnumObjectType;
import stellarapi.api.lib.math.Vector3;

public class SAPISun extends CelestialObject {
	public SAPISun(double day, double dayOffset) {
		super(new ResourceLocation("sun"), EnumObjectType.Star);
		this.setHoritontalPeriod(new CelestialPeriod("Day", day, dayOffset / day));
		this.setPos(new Vector3(1.0, 0.0, 0.0));
		this.setStandardMagnitude(-26.74);
	}
}
