package stellarapi.feature.celestial.tweakable;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.celestials.EnumObjectType;
import stellarapi.api.lib.math.Vector3;
import stellarapi.impl.celestial.CelestialQuadObject;

public class SAPISun extends CelestialQuadObject {
	private static final Vector3[] VERTICES = new Vector3[] {
			new Vector3(1.0, -0.3, -0.3),
			new Vector3(1.0, -0.3, 0.3),
			new Vector3(1.0, 0.3, 0.3),
			new Vector3(1.0, 0.3, -0.3)
	};

	public SAPISun(double day, double dayOffset) {
		super(new ResourceLocation("sun"), EnumObjectType.Star,
				new Vector3(1.0, 0.0, 0.0), VERTICES);
		this.setHorizontalPeriod(new CelestialPeriod("Day", day, dayOffset / day));
		this.setStandardMagnitude(-26.74);
	}
}
