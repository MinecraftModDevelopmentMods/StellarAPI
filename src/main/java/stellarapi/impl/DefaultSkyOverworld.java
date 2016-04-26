package stellarapi.impl;

import stellarapi.api.ISkyEffect;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.optics.Wavelength;

public class DefaultSkyOverworld implements ISkyEffect {

	@Override
	public void applyAtmRefraction(SpCoord pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disapplyAtmRefraction(SpCoord pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public float calculateAirmass(SpCoord pos) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getExtinctionRate(Wavelength wavelength) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSeeing(Wavelength wl) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getAbsorptionFactor(float partialTicks) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getDispersionFactor(Wavelength wavelength, float partialTicks) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getLightPollutionFactor(Wavelength wavelength, float partialTicks) {
		// TODO Auto-generated method stub
		return 0;
	}

}
