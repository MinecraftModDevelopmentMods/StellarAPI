package stellarium.world;

import com.google.common.collect.ImmutableMap;

import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.optics.WaveIntensive;
import stellarapi.api.optics.WaveFilterType;
import stellarium.util.math.StellarMath;

public class NonRefractiveSkySet implements IStellarSkySet {
	
	private boolean hideObjectsUnderHorizon;
	private float lightPollutionFactor, dispersionFactor, minimumSkyRenderBrightness;
	
	private WaveIntensive interpolation;
	
	public NonRefractiveSkySet(PerDimensionSettings settings) {
		this.hideObjectsUnderHorizon = settings.hideObjectsUnderHorizon();
		this.dispersionFactor = (float) settings.getSkyDispersionRate();
		this.lightPollutionFactor = (float) settings.getLightPollutionRate();
		this.minimumSkyRenderBrightness = (float) settings.getMinimumSkyRenderBrightness();
		
		double[] rates = settings.extinctionRates();
		this.interpolation = new WaveIntensive(
				ImmutableMap.of(
						WaveFilterType.red, StellarMath.MagToLumWithoutSize(rates[0]),
						WaveFilterType.V, StellarMath.MagToLumWithoutSize(rates[1]),
						WaveFilterType.B,StellarMath.MagToLumWithoutSize(rates[2]))
				);
	}

	@Override
	public void applyAtmRefraction(SpCoord coord) { }

	@Override
	public void disapplyAtmRefraction(SpCoord coord) { }

	@Override
	public float calculateAirmass(SpCoord coord) {
		return 0.0f;
	}

	@Override
	public boolean hideObjectsUnderHorizon() {
		return this.hideObjectsUnderHorizon;
	}

	@Override
	public float getAbsorptionFactor(float partialTicks) {
		//Assume that there is no absorption.
		return 0.0f;
	}

	@Override
	public float getDispersionFactor(WaveFilterType wavelength, float partialTicks) {
		return this.dispersionFactor;
	}

	@Override
	public float getExtinctionRate(WaveFilterType wavelength) {
		return (float) StellarMath.LumToMagWithoutSize(interpolation.apply(wavelength).doubleValue());
	}

	@Override
	public float getLightPollutionFactor(WaveFilterType wavelength, float partialTicks) {
		return this.lightPollutionFactor;
	}

	@Override
	public double getSeeing(WaveFilterType wavelength) {
		return 0.0;
	}

	@Override
	public float minimumSkyRenderBrightness() {
		return this.minimumSkyRenderBrightness;
	}

}
