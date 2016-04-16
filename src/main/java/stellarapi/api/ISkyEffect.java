package stellarapi.api;

public interface ISkyEffect {
	
	/**
	 * Gets absorption factor. <p>
	 * Affects the ground brightness by absorbing lights from celestial light source,
	 *  and does not affect the brightness of sky.
	 * @param partialTicks the partial tick
	 * */
	public float getAbsorptionFactor(float partialTicks);
	
	/**
	 * Gets dispersion factor. <p>
	 * Affects the brightness of sky, and does not affect the ground brightness.
	 * @param partialTicks the partial tick
	 * */
	public float getDispersionFactor(float partialTicks);
	
	/**
	 * Gets light pollution factor. <p> 
	 * Affects effect of ground light source to the brightness of sky. (This is called 'light pollution')
	 * @param partialTicks the partial tick
	 * */
	public float getLightPollutionFactor(float partialTicks);

}
