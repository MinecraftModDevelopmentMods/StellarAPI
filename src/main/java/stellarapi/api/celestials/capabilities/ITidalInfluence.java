package stellarapi.api.celestials.capabilities;

import stellarapi.api.lib.math.Vector3;

/**
 * Tidal influences of the celestial object.
 * */
public interface ITidalInfluence {

	/**
	 * Tidal force relative to the gravity.
	 * Should be small.
	 * */
	public double tidalForce();

	/**
	 * Current tide direction.
	 * */
	public Vector3 currentTideDirection();

}