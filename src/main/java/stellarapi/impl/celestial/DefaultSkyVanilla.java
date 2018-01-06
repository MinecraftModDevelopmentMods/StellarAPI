package stellarapi.impl.celestial;

import stellarapi.api.ISkyEffect;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.optics.Wavelength;

/**
 * Default implementation of sky.
 */
public class DefaultSkyVanilla implements ISkyEffect {

	@Override
	public void applyAtmRefraction(SpCoord pos) {
		return;
	}

	@Override
	public void disapplyAtmRefraction(SpCoord pos) {
		return;
	}

	@Override
	public float calculateAirmass(SpCoord pos) {
		return 0.0f;
	}

	@Override
	public float getExtinctionRate(Wavelength wavelength) {
		return 0.0f;
	}

	@Override
	public double getSeeing(Wavelength wl) {
		return 0.0f;
	}

	@Override
	public float getAbsorptionFactor(float partialTicks) {
		return 0.0f;
	}

	@Override
	public float getDispersionFactor(Wavelength wavelength, float partialTicks) {
		return 1.0f;
	}

	@Override
	public float getLightPollutionFactor(Wavelength wavelength, float partialTicks) {
		return 0.0f;
	}

	@Override
	public float minimumSkyRenderBrightness() {
		return 0.2f;
	}

}
