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
	 * Gets height angle when the object is on highest during the horizontal period. <p>
	 * Useful for checking highest time.
	 * @param absPos the absolute position of the object
	 * */
	public double getHighestHeightAngle(Vector3d absPos);
	
	/**
	 * Offset on the horizontal period, that is needed till object reaches certain height angle first.
	 * @param absPos the absolute position of the object
	 * @param heightAngle the height angle in degrees
	 * */
	public double offsetTillObjectReach(Vector3d absPos, double heightAngle);

}