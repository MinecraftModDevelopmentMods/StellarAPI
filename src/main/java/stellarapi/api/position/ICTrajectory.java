package stellarapi.api.position;

import stellarapi.api.lib.math.Vector3;

/**
 * Celestial trajectory.
 * Currently, it should be either periodic or constant, and should be smooth on time.
 * */
public interface ICTrajectory {
	/**
	 * Gets the period.
	 * If it has no period, gives {@link Double#POSITIVE_INFINITY}.
	 * */
	public double getPeriod();

	/**
	 * Gets the position for the given time.
	 * */
	public Vector3 getPosition(double time);

	/**
	 * Gets the current position.
	 * */
	public Vector3 getPosition();
}
