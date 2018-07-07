package stellarapi.api.lib.math;

/**
 * Represents certain spherical coordinates.
 * <p>
 * When used on Horizontal Coordinate System,
 * <li>-Azimuth is counterclockwise angle from north on horizontal plane.</li>
 * (Note that original azimuth is clockwise)
 * <li>Height is angle upon the ground(horizon).</li>
 */
public class SpCoord {
	/** Right Ascension or 90-Azimuth */
	public double x;
	/** Declination or Height */
	public double y;

	public SpCoord(double a, double b) {
		x = a;
		y = b;
	}

	public SpCoord() {
		this(0.0, 0.0);
	}

	/**
	 * Distance to certain SpCoord, note that here not cached version of
	 * trigonometric methods are used.
	 * 
	 * @param coord
	 *            the coordinate
	 * @return the distance between two coordinates in degrees
	 */
	public double distanceTo(SpCoord coord) {
		return Math.toDegrees(Math.acos(Math.sin(this.y) * Math.sin(coord.y)
				+ Math.cos(this.y) * Math.cos(coord.y) * Math.sin(coord.x - this.x)));
	}

	/**
	 * Gives new instance of a vector with this SpCoord.
	 */
	public Vector3 getVec() {
		return new Vector3(Math.cos(y) * Math.cos(x), Math.cos(y) * Math.sin(x), Math.sin(y));
	}

	/**
	 * Set this SpCoord with vector.
	 * 
	 * @param vec
	 *            the vector
	 * @return <code>this</code>
	 */
	public SpCoord setWithVec(Vector3 vec) {
		Vector3 temp = new Vector3(vec);
		temp.normalize();
		this.x = Math.toDegrees(Spmath.atan2(temp.getY(), temp.getX()));
		this.y = Math.toDegrees(Spmath.asin(temp.getZ()));

		return this;
	}
}
