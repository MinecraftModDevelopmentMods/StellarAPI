package stellarapi.impl.celestial;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.celestials.EnumObjectType;
import stellarapi.api.lib.math.Vector3;

public class DefaultSun extends CelestialObject {
	public DefaultSun() {
		super(new ResourceLocation("sun"), EnumObjectType.Star);

		this.setHoritontalPeriod(new CelestialPeriod("Day", 24000.0, 0.25));
		this.setPos(new Vector3(1.0, 0.0, 0.0));
		this.setStandardMagnitude(-26.74);
	}
}
