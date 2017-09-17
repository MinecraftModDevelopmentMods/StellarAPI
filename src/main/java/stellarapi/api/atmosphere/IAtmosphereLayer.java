package stellarapi.api.atmosphere;

import java.util.Map;

import stellarapi.api.optics.Wavelength;

public interface IAtmosphereLayer {
	// TODO Atmosphere Correctly define each layer.

	/**
	 * Density characteristics in specific data points.
	 * This is multiplied to the general exponential curve.
	 * The values shoouldn't be far from 1.0
	 * */
	public Map<Double, Double> densityCharacteristics();

	/**
	 * Gets the absorption rate for specific wavelength.
	 * */
	public double getAbsorption(Wavelength wave);

	/**
	 * Gets the scatter rate for specific wavelength and angle.
	 * */
	public double getScatterRate(Wavelength wave, double angle);
}