package stellarapi.impl.celestial;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.celestials.EnumObjectType;
import stellarapi.api.lib.math.Vector3;

public class DefaultSun extends CelestialQuadObject {
	private static final Vector3[] VERTICES = new Vector3[] {
			new Vector3(1.0, -0.3, -0.3),
			new Vector3(1.0, -0.3, 0.3),
			new Vector3(1.0, 0.3, 0.3),
			new Vector3(1.0, 0.3, -0.3)
	};

	public DefaultSun() {
		super(new ResourceLocation("sun"), EnumObjectType.Star,
				new Vector3(1.0, 0.0, 0.0), VERTICES);
		this.setHorizontalPeriod(new CelestialPeriod("Day", 24000.0, 0.25));
		this.setStandardMagnitude(-26.74);
	}
}
