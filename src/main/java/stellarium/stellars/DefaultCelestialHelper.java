package stellarium.stellars;

import stellarapi.example.CelestialHelperExample;
import stellarium.api.ICelestialHelper;

public class DefaultCelestialHelper extends CelestialHelperExample implements ICelestialHelper {

	public DefaultCelestialHelper(float relativeMultiplierSun, float relativeMultiplierMoon, ICelestialObject sun,
			ICelestialObject moon, ICelestialCoordinate coordinate, ISkyEffect sky) {
		super(relativeMultiplierSun, relativeMultiplierMoon, sun, moon, coordinate, sky);
	}

}
