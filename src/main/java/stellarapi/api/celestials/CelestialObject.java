package stellarapi.api.celestials;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.optics.Wavelength;
import stellarapi.api.view.ICCoordinates;

/**
 * Celestial Object.
 */
public class CelestialObject {
	private final ResourceLocation name;
	private final EnumObjectType type;

	private CelestialPeriod absolutePeriod = null, horizontalPeriod = null, phasePeriod = null;

	private Vector3 pos = null;
	private double standardMagnitude;

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
	public @Nullable CelestialPeriod getAbsolutePeriod() {
		return this.absolutePeriod;
	}

	/**
	 * Horizontal period for the effector.<p>
	 * This is the period that the horizontal position of this effector returns
	 * to original position.<p>
	 * Normally starts from the lowest position.<p>
	 * Will be removed when proper calculation for period takes in place.
	 * 
	 * @param coords the coordinates
	 * @return
	 *         <ul>
	 *         <li>horizontal period object for this effector if it exists
	 *         <li><b><code>null</code></b> if this effector is stopped in
	 *         horizontal coordinate or have sufficiently fast random movements.
	 *         </ul>
	 */
	@Deprecated
	public @Nullable CelestialPeriod getHorizontalPeriod(ICCoordinates coords) {
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
	public @Nullable CelestialPeriod getPhasePeriod() {
		return this.phasePeriod;
	}


	protected void setAbsolutePeriod(CelestialPeriod period) {
		this.absolutePeriod = period;
	}

	@Deprecated
	protected void setHorizontalPeriod(CelestialPeriod period) {
		this.horizontalPeriod = period;
	}

	protected void setPhasePeriod(CelestialPeriod period) {
		this.phasePeriod = period;
	}


	protected void setPos(Vector3 posIn) {
		this.pos = posIn;
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
	 * <code>null</code> if this object doesn't have specific position.
	 */
	public @Nullable Vector3 getCurrentPos() {
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
