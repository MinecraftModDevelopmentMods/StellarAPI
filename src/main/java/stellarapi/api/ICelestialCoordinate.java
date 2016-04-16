
package stellarapi.api;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3d;

public interface ICelestialCoordinate {
	
	/**
	 * Gets projection which projects absolute position to ground position. <p>
	 * Note that one can construct reverse projection by getting transpose of this matrix.
	 * */
	public Matrix3f getProjectionToGround();
	
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
	 * The least offset on the horizontal period, that is needed till object reaches certain height angle. <p>
	 * Will be in range [0, 0.5) in most case.
	 * @param absPos the absolute position of the object
	 * @param heightAngle the height angle in degrees
	 * @return the offset if it exists, or <code>NaN</code> if the object cannot reach certain height angle.
	 * */
	public double offsetTillObjectReach(Vector3d absPos, double heightAngle);

}