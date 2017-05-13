package stellarapi.api.celestials.capabilities;

import stellarapi.api.CelestialPeriod;
import stellarapi.api.lib.math.Vector3;

/**
 * Absolute periodic movement of the object, totally .
 * */
public interface IPeriodicMovement {

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
	 * The pole of Absolute Periodic Movement.
	 * */
	public Vector3 getAbsolutePeriodPole();

}
