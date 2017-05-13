package stellarapi.api.celestials.capabilities;

import stellarapi.api.CelestialPeriod;

public interface IPhaseInfo {

	/**
	 * Gets current phase intensity of this phased object.
	 */
	public double getCurrentPhase();

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

}
