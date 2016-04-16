package stellarapi.api.impl;

import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.ICelestialObject;
import stellarapi.api.ISkyEffect;
import stellarapi.util.math.Spmath;

/**
 * Helper for WorldProvider. <p>
 * Note that this can be only used when <p>
 * <ul>
 *  <li> There is one Sun and one Moon.
 *  <li>{@link ICelestialObject#getHorizontalPeriod()} is not null for Sun and Moon.
 *  <li>{@link ICelestialObject#getPhasePeriod()} is not null for Moon.
 * </ul>
 * */
public class DefaultCelestialHelper {
	
	private final float relativeMultiplierSun;
	private final float relativeMultiplierMoon;
	
	private final ICelestialObject sun;
	private final ICelestialObject moon;
	
	private final ICelestialCoordinate coordinate;
	
	private final ISkyEffect sky;
	
	public DefaultCelestialHelper(float relativeMultiplierSun, float relativeMultiplierMoon,
			ICelestialObject sun, ICelestialObject moon,
			ICelestialCoordinate coordinate, ISkyEffect sky) {
		this.relativeMultiplierSun = relativeMultiplierSun;
		this.relativeMultiplierMoon = relativeMultiplierMoon;
		this.sun = sun;
		this.moon = moon;
		this.coordinate = coordinate;
		this.sky = sky;
	}
	
	/**
	 * Calculates current celestial angle. <p>
	 * Basically for WorldProvider. <p>
	 * @param worldTime current world time
	 * @param partialTicks the partial tick
	 * */
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		return (float) sun.getHorizontalPeriod().getOffset(worldTime, partialTicks) + 0.25f;
	}
	
	/**
	 * Gets current sun height rate.
	 * @param partialTicks the partial tick
	 * @return <code>sin(Height_Angle_Of_Sun)</code>
	 * */
	public float getSunHeightFactor(float partialTicks) {
		return (float) Spmath.sind(sun.getCurrentHorizontalPos().y);
	}
	
	/**
	 * Gets current sunlight factor. <p>
	 * @param partialTicks the partial tick
	 * */
	public float getSunlightFactor(float partialTicks) {
		return (float) ((2.0*this.getSunHeightFactor(partialTicks)+0.5)
				* sun.getCurrentBrightness()) * this.relativeMultiplierSun;
	}
	
	/**
	 * Gets current sunrise sunset factor(brightness of sunrise/sunset color).
	 * @param partialTicks the partial tick
	 * */
	public float calculateSunriseSunsetFactor(float partialTicks) {
		return (float) Math.sqrt(sun.getCurrentBrightness() * this.relativeMultiplierSun);
	}
	
	/**
	 * Gets dispersion factor. (brightness of sky itself, not ground light level) <p>
	 * @param partialTicks the partial tick
	 * */
	public float getDispersionFactor(float partialTicks) {
		return sky.getDispersionFactor(partialTicks);
	}
	
	/**
	 * Gets light pollution factor.
	 * @param partialTicks the partial tick
	 * */
	public float getLightPollutionFactor(float partialTicks) {
		return sky.getLightPollutionFactor(partialTicks);
	}
	
	/**
	 * Gets current moon phase time.
	 * @param worldTime the current World Time. If it isn't, this method will give undefined result.
	 * */
	public int getCurrentMoonPhase(long worldTime) {
		return (int) Math.floor(moon.getPhasePeriod().getOffset(worldTime, 0.0f) * 8);
	}
	
	/**
	 * Gets current moon phase factor. <p>
	 * */
	public float getCurrentMoonPhaseFactor() {
		return (float) moon.getCurrentPhase() * this.relativeMultiplierMoon;
	}

}
