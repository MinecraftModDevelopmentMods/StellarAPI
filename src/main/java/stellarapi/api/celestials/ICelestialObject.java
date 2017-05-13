package stellarapi.api.celestials;

import stellarapi.api.CelestialPeriod;
import stellarapi.api.lib.math.SpCoord;
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

	/**
	 * The name of the celestial object.
	 */
	public String getName();


	public CelestialPeriod getAbsolutePeriod();

	/**
	 * Horizontal period for the effector.
	 * <p>
	 * This is the period that the horizontal position of this effector returns
	 * to original position.
	 * <p>
	 * Normally starts from the lowest position.
	 * 
	 * @return
	 *         <ul>
	 *         <li>horizontal period object for this effector if it exists
	 *         <li><b><code>null</code></b> if this effector is stopped in
	 *         horizontal coordinate or have sufficiently fast random movements.
	 *         </ul>
	 */
	public CelestialPeriod getHorizontalPeriod();

	/** Gets current position for horizontal coordinate. */
	public SpCoord getCurrentHorizontalPos();
}
