
package stellarapi.api;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

public interface ICelestialCoordinate {
	
	/**
	 * Gets projection which projects absolute position to ground position. <p>
	 * Note that one can construct reverse projection by getting transpose of this matrix.
	 * */
	public Matrix3d getProjectionToGround();
	
	/**
	 * Period of the coordinate.
	 * */
	public CelestialPeriod getPeriod();
	
	/**
	 * Gets height angle when the object is on highest position during the horizontal period. <p>
	 * Useful for checking highest time. <p>
	 * (It is offset 0.5 in horizontal period in most case)
	 * @param absPos the absolute position of the object
	 * */
	public double getHighestHeightAngle(Vector3d absPos);
	
	/**
	 * Gets height angle when the object is on lowest position during the horizontal period. <p>
	 * Useful for checking lowest time. <p>
	 * (It is offset 0.0 in horizontal period in most case)
	 * @param absPos the absolute position of the object
	 * */
	public double getLowestHeightAngle(Vector3d absPos);
	
	/**
	 * The initial offset of certain absolute position on the horizontal period. <p>
	 * Helper method for getting horizontal period of a celestial object. <p>
	 * Should be available right after coordinate is registered.
	 * @param absPos the absolute position of the object
	 * @param periodLength the length of the period, exist for the case which time 0 on sky is different from tick 0.
	 * @param heightAngle the height angle in degrees
	 * @return the initial offset for the initial absolute position
	 * */
	public double calculateInitialOffset(Vector3d initialAbsPos, double periodLength);
	
	/**
	 * The least offset on the horizontal period, that is needed till object reaches certain height angle. <p>
	 * Will be in range [0, 0.5) in most case.
	 * @param absPos the absolute position of the object
	 * @param heightAngle the height angle in degrees
	 * @return the offset if it exists, or <code>NaN</code> if the object cannot reach certain height angle
	 * */
	public double offsetTillObjectReach(Vector3d absPos, double heightAngle);

}