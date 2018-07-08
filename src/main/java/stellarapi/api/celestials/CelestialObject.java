package stellarapi.api.celestials;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.optics.Wavelength;

/**
 * Celestial Object.
 */
public class CelestialObject {
	private final ResourceLocation name;
	private final EnumObjectType type;

	private CelestialPeriod absolutePeriod = null, horizontalPeriod = null, phasePeriod = null;

	protected Vector3 pos;
	protected double standardMagnitude;

	public CelestialObject(ResourceLocation nameIn, EnumObjectType typeIn) {
		this.name = nameIn;
		this.type = typeIn;
	}

	/** The name of the celestial object. */
	public ResourceLocation getName() {
		return this.name;
	}

	/**
	 * Gets type of this object.
	 */
	public EnumObjectType getObjectType() {
		return this.type;
	}

	protected void setPos(Vector3 posIn) {
		this.pos = posIn;
	}

	protected void setStandardMagnitude(double magnitude) {
		this.standardMagnitude = magnitude;
	}

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
	public CelestialPeriod getAbsolutePeriod() {
		return this.absolutePeriod;
	}

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
	public CelestialPeriod getHorizontalPeriod() {
		return this.horizontalPeriod;
	}

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
	public CelestialPeriod getPhasePeriod() {
		return this.phasePeriod;
	}


	protected void setAbsolutePeriod(CelestialPeriod period) {
		this.absolutePeriod = period;
	}

	protected void setHoritontalPeriod(CelestialPeriod period) {
		this.horizontalPeriod = period;
	}

	protected void setPhasePeriod(CelestialPeriod period) {
		this.phasePeriod = period;
	}

	/**
	 * Gets current phase of this effector. Meaningful only if this object has phase.
	 */
	public double getCurrentPhase() {
		return 0.0;
	}

	/**
	 * Gets current relative brightness to the standard brightness.
	 * 
	 * @param wavelength
	 *            the wavelength to get certain brightness on
	 */
	public double getCurrentBrightness(Wavelength wavelength) {
		return 1.0;
	}

	/**
	 * Gets current absolute position.
	 */
	public Vector3 getCurrentPos() {
		return this.pos;
	}

	/**
	 * Gets standard visible magnitude of this object.
	 * <p>
	 * Should be constant.
	 */
	public double getStandardMagnitude() {
		return this.standardMagnitude;
	}

}
