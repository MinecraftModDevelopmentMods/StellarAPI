package stellarapi.example;

import javax.annotation.Nullable;

import net.minecraft.util.math.MathHelper;
import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.lib.math.Spmath;
import stellarapi.api.optics.EnumRGBA;
import stellarapi.api.optics.Wavelength;
import stellarapi.api.view.IAtmosphereEffect;
import stellarapi.api.view.ICCoordinates;
import stellarapi.api.view.ViewUtil;
import stellarapi.api.world.ICelestialHelper;

/**
 * Helper for WorldProvider.
 * <p>
 * Note that this can be only used when
 * <p>
 * <ul>
 * <li>There is one Sun and one Moon.
 * <li>{@link CelestialObject#getHorizontalPeriod()} is not null for Sun and
 * Moon.
 * <li>{@link CelestialObject#getPhasePeriod()} is not null for Moon.
 * </ul>
 */
public class CelestialHelperSimple implements ICelestialHelper {

	private final float relativeMultiplierSun;
	private final float relativeMultiplierMoon;

	private final @Nullable CelestialObject sun;
	private final @Nullable CelestialObject moon;

	private final ICCoordinates coordinate;

	private final IAtmosphereEffect sky;

	public CelestialHelperSimple(float relativeMultiplierSun, float relativeMultiplierMoon,
			@Nullable CelestialObject sun, @Nullable CelestialObject moon,
			ICCoordinates coordinate, IAtmosphereEffect sky) {
		this.relativeMultiplierSun = relativeMultiplierSun;
		this.relativeMultiplierMoon = relativeMultiplierMoon;
		this.sun = sun;
		this.moon = moon;
		this.coordinate = coordinate;
		this.sky = sky;
	}

	/**
	 * Calculates current celestial angle.
	 * <p>
	 * Basically for WorldProvider.
	 * <p>
	 * 
	 * @param worldTime
	 *            current world time
	 * @param partialTicks
	 *            the partial tick
	 */
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		if(this.sun != null)
			return (float) sun.getHorizontalPeriod().getBiasedOffset(worldTime, partialTicks, 0.5);
		else return (float) coordinate.getPeriod().getOffset(worldTime, partialTicks);
	}

	/**
	 * Gets current sun height rate.
	 * 
	 * @param partialTicks
	 *            the partial tick
	 * @return <code>sin(Height_Angle_Of_Sun)</code>
	 */
	@Override
	public float getSunHeightFactor(float partialTicks) {
		SpCoord sunCoord = ViewUtil.transformToHor(this.coordinate, this.sky, sun.getCurrentPos());
		if(this.sun != null)
			return (float) Math.sin(Math.toRadians(sunCoord.y));
		else return 0.0f;
	}

	/**
	 * Gets current sunlight factor.
	 * 
	 * @param color
	 *            the color to get sunlight factor
	 * @param partialTicks
	 *            the partial tick
	 */
	@Override
	public float getSunlightFactor(EnumRGBA color, float partialTicks) {
		if(this.sun != null)
			return MathHelper.clamp(2.0f * this.getSunHeightFactor(partialTicks) + 0.5f, 0.0f, 1.0f)
					* (float) sun.getCurrentBrightness(Wavelength.colorWaveMap.get(color)) * this.relativeMultiplierSun;
		else return 0.0f;
	}

	/**
	 * Gets current sunlight factor which affects rendering of the sky.
	 * 
	 * @param color
	 *            the color to get sunlight factor
	 * @param partialTicks
	 *            the partial tick
	 */
	@Override
	public float getSunlightRenderBrightnessFactor(float partialTicks) {
		if(this.sun != null)
			return MathHelper.clamp(2.0f * this.getSunHeightFactor(partialTicks) + 0.2f, 0.0f, 1.0f)
					* (float) sun.getCurrentBrightness(Wavelength.colorWaveMap.get(EnumRGBA.Alpha))
					* this.relativeMultiplierSun;
		else return 0.0f;
	}

	/**
	 * Gets current sky transmission factor.
	 * 
	 * @param partialTicks
	 *            the partial tick
	 */
	@Override
	public float getSkyTransmissionFactor(float partialTicks) {
		return 1.0f - sky.getAbsorptionFactor(partialTicks);
	}

	/**
	 * Gets current sunrise sunset factor(brightness of sunrise/sunset color).
	 * 
	 * @param partialTicks
	 *            the partial tick
	 */
	@Override
	public float calculateSunriseSunsetFactor(EnumRGBA color, float partialTicks) {
		if(this.sun != null)
			return (float) (sun.getCurrentBrightness(Wavelength.colorWaveMap.get(color))) * this.relativeMultiplierSun;
		else return 0.0f;
	}

	/**
	 * Gets dispersion factor. (brightness of sky itself, not ground light
	 * level)
	 * <p>
	 * 
	 * @param partialTicks
	 *            the partial tick
	 */
	@Override
	public float getDispersionFactor(EnumRGBA color, float partialTicks) {
		return sky.getDispersionFactor(Wavelength.colorWaveMap.get(color), partialTicks);
	}

	/**
	 * Gets light pollution factor.
	 * 
	 * @param partialTicks
	 *            the partial tick
	 */
	@Override
	public float getLightPollutionFactor(EnumRGBA color, float partialTicks) {
		return sky.getLightPollutionFactor(Wavelength.colorWaveMap.get(color), partialTicks);
	}

	/**
	 * Gets current moon phase time.
	 * 
	 * @param worldTime
	 *            the current World Time. If it isn't, this method will give
	 *            undefined result.
	 */
	@Override
	public int getCurrentMoonPhase(long worldTime) {
		if(this.moon != null)
			return (int) Math.floor(moon.getPhasePeriod().getBiasedOffset(worldTime, 0.0f, 0.5) * 8);
		else return 0;
	}

	/**
	 * Gets current moon phase factor.
	 * <p>
	 */
	@Override
	public float getCurrentMoonPhaseFactor() {
		if(this.moon != null)
			return (float) moon.getCurrentPhase() * this.relativeMultiplierMoon;
		else return 0.0f;
	}

	@Override
	public float minimumSkyRenderBrightness() {
		return sky.minimumSkyRenderBrightness();
	}

}
