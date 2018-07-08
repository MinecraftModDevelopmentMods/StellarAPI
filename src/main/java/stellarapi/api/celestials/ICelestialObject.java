package stellarapi.api.celestials;

import stellarapi.api.CelestialPeriod;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.optics.Wavelength;

/**
 * Celestial Object interface.
 * <p>
 * Note that this object is not the static celestial object itself, but the
 * image of the object which can be seen from specific world.
 * <p>
 * So this object is, indeed, per-dimensional.
 */
public interface ICelestialObject {
	// TODO AA ALL ResourceLocation?
	/** The name of the celestial object. */
	public String getName();

	/**
	 * Absolute period for the effector.
	 * <p>
	 * This is the period that the absolute position of this effector returns to
	 * original position.
	 * <p>
	 * Normally starts from the rising position(ascending node).
	 * 
	 * @return
	 *         <ul>
	 *         <li>absolute period object for this effector if it exists
	 *         <li><b><code>null</code></b> if this effector is stopped or have
	 *         random movements.
	 *         </ul>
	 */
	public CelestialPeriod getAbsolutePeriod();

	/**
	 * Horizontal period for the effector.<p>
	 * This is the period that the horizontal position of this effector returns
	 * to original position.<p>
	 * Normally starts from the lowest position.<p>
	 * Will be removed when proper calculation for period takes in place
	 * 
	 * @return
	 *         <ul>
	 *         <li>horizontal period object for this effector if it exists
	 *         <li><b><code>null</code></b> if this effector is stopped in
	 *         horizontal coordinate or have sufficiently fast random movements.
	 *         </ul>
	 */
	@Deprecated
	public CelestialPeriod getHorizontalPeriod();

	/**
	 * Phase period for this effector. Normally starts from the darkest phase.
	 * 
	 * @return
	 *         <ul>
	 *         <li>phase period object for this effector,
	 *         <li><b><code>null</code></b> if this effector doesn't have phase,
	 *         e.g. sun.
	 *         </ul>
	 */
	public CelestialPeriod getPhasePeriod();

	/**
	 * Gets current phase of this effector.
	 */
	public double getCurrentPhase();

	/**
	 * Gets current relative brightness to the standard brightness.
	 * 
	 * @param wavelength
	 *            the wavelength to get certain brightness on
	 */
	public double getCurrentBrightness(Wavelength wavelength);

	/**
	 * Gets current absolute position.
	 */
	public Vector3 getCurrentPos();

	/**
	 * Gets standard visible magnitude of this object.
	 * <p>
	 * Should be constant.
	 */
	public double getStandardMagnitude();

	/**
	 * Gets type of this object.
	 */
	public EnumObjectType getObjectType();

}
