package stellarapi.api.celestials;

import stellarapi.api.CelestialPeriod;
import stellarapi.api.optics.Wavelength;
import stellarapi.api.position.ICTrajectory;

/**
 * Celestial Object interface.
 * <p>
 * Note that this object is not the static celestial object itself, but the
 * image of the object which can be seen from specific world.
 * <p>
 * So this object is, indeed, per-dimensional.
 */
public interface ICelestialObject {

	/**
	 * The name of the celestial object.
	 */
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
	 * Gets current relative brightness to the maximum brightness.
	 * 
	 * @param wavelength
	 *            the wavelength to get certain brightness on
	 */
	public double getCurrentBrightness(Wavelength wavelength);

	/** Gets current trajectory of this object. */
	public ICTrajectory getTrajectory();

	/** Adds a tracker. */
	public void addTracker(ICObjectTracker tracker);

	/**
	 * Gets standard visible magnitude of this object.
	 * <p>
	 * Should be constant.
	 */
	public double getStandardMagnitude();

	/**
	 * Gets type of this object.
	 */
	public EnumCelestialObjectType getObjectType();

}
