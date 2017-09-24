package stellarapi.api.celestials.capabilities;

import stellarapi.api.optics.WaveFilterType;

/**
 * Optical properties of the celestial object.
 * */
public interface ICelestialOptics {

	/**
	 * Gets the standard magnitude of the object.
	 * */
	public double getStandardMagnitude();

	/**
	 * Current relative brightness to the maximum brightness.
	 * 
	 * @param wavelength the wavelength to get certain brightness on
	 */
	public double getCurrentBrightness(WaveFilterType wavelength);

}