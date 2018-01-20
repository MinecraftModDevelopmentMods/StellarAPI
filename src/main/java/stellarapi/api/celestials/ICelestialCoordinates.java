
package stellarapi.api.celestials;

import stellarapi.api.CelestialPeriod;
import stellarapi.api.lib.math.Matrix3;
import stellarapi.api.lib.math.Vector3;

public interface ICelestialCoordinates {

	/**
	 * Gets projection which projects absolute position to ground position.
	 * <p>
	 * Basis of ground coordinates are x : East, y : North, z : Zenith.
	 * <p>
	 * Note that one can construct reverse projection by getting transpose of
	 * this matrix.
	 */
	public Matrix3 getProjectionToGround();

	/**
	 * Period of the coordinate.
	 */
	public CelestialPeriod getPeriod();

	/**
	 * Gets height angle when the object is on highest position during the
	 * horizontal period.
	 * <p>
	 * Useful for checking highest time.
	 * <p>
	 * (It is offset 0.5 in horizontal period in most case)
	 * 
	 * @param absPos
	 *            the absolute position of the object
	 */
	public double getHighestHeightAngle(Vector3 absPos);

	/**
	 * Gets height angle when the object is on lowest position during the
	 * horizontal period.
	 * <p>
	 * Useful for checking lowest time.
	 * <p>
	 * (It is offset 0.0 in horizontal period in most case)
	 * 
	 * @param absPos
	 *            the absolute position of the object
	 */
	public double getLowestHeightAngle(Vector3 absPos);

	/**
	 * The initial offset of certain absolute position on the horizontal period.
	 * <p>
	 * Helper method for calculating horizontal period of a celestial object.
	 * <p>
	 * Should be available right after coordinate is registered.
	 * 
	 * @param initialAbsPos
	 *            the absolute position of the object
	 * @param periodLength
	 *            the length of the period, exist for the case which time 0 on
	 *            sky is different from tick 0.
	 * @return the initial offset for the initial absolute position
	 */
	public double calculateInitialOffset(Vector3 initialAbsPos, double periodLength);

	/**
	 * The least offset on the horizontal period, that is needed till object
	 * reaches certain height angle.
	 * <p>
	 * Will be in range [0, 0.5) in most case.
	 * 
	 * @param absPos
	 *            the absolute position of the object
	 * @param heightAngle
	 *            the height angle in degrees
	 * @return the offset if it exists, or <code>NaN</code> if the object cannot
	 *         reach certain height angle
	 */
	public double offsetTillObjectReach(Vector3 absPos, double heightAngle);

}