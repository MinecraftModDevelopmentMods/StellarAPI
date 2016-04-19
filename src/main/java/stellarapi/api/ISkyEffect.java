package stellarapi.api;

import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.optics.Wavelength;

/**
 * Interface which represents the sky effect.
 * */
public interface ISkyEffect {
	
	/**
	 * Applies atmospheric refraction to horizontal spherical position.
	 * @param pos the horizontal spherical position
	 * */
	public void applyAtmRefraction(SpCoord pos);
	
	/**
	 * Disapply atmospheric refraction to horizontal spherical position.
	 * @param pos the horizontal spherical position
	 * */
	public void disapplyAtmRefraction(SpCoord pos);
	
	/**
	 * Calculates airmass for certain horizontal spherical position.
	 * @param pos the horizontal spherical position
	 * */
	public float calculateAirmass(SpCoord pos);
	
	/**
	 * Gets extinction rate for certain wavelength.
	 * @param wavelength the wavelength
	 * */
	public float getExtinctionRate(Wavelength wavelength);
	
	/**
	 * Seeing for certain wavelength.
	 * @param wavelength the wavelength
	 * */
	public double getSeeing(Wavelength wl);
	
	/**
	 * Gets light absorption factor. <p>
	 * Affects the ground brightness by absorbing lights from celestial light source,
	 *  and does not affect the brightness of sky.
	 * @param partialTicks the partial tick
	 * */
	public float getAbsorptionFactor(float partialTicks);
	
	/**
	 * Gets light dispersion factor. <p>
	 * Affects the brightness of sky, and does not affect the ground brightness.
	 * @param wavelength the wavelength
	 * @param partialTicks the partial tick
	 * */
	public float getDispersionFactor(Wavelength wavelength, float partialTicks);
	
	/**
	 * Gets light pollution factor. <p> 
	 * Affects effect of ground light source to the brightness of sky. (This is called 'light pollution')
	 * @param wavelength the wavelength
	 * @param partialTicks the partial tick
	 * */
	public float getLightPollutionFactor(Wavelength wavelength, float partialTicks);

}
