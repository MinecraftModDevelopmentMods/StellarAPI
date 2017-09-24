package stellarapi.impl;

import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.optics.WaveFilterType;

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
	public float getExtinctionRate(WaveFilterType wavelength) {
		return 0.0f;
	}

	@Override
	public double getSeeing(WaveFilterType wl) {
		return 0.0f;
	}

	@Override
	public float getAbsorptionFactor(float partialTicks) {
		return 0.0f;
	}

	@Override
	public float getDispersionFactor(WaveFilterType wavelength, float partialTicks) {
		return 1.0f;
	}

	@Override
	public float getLightPollutionFactor(WaveFilterType wavelength, float partialTicks) {
		return 0.0f;
	}

	@Override
	public float minimumSkyRenderBrightness() {
		return 0.2f;
	}

}
