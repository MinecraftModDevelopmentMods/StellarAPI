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

	public SpCoord set(SpCoord coord) {
		this.x = coord.x;
		this.y = coord.y;
		return this;
	}

	/**
	 * Distance to certain SpCoord, note that here not cached version of
	 * trigonometric methods are used.
	 * 
	 * @param coord the coordinate
	 * @return the distance between two coordinates in degrees
	 */
	public double distanceTo(SpCoord coord) {
		return Math.toDegrees(Math.acos(sinDeg(this.y) * sinDeg(coord.y)
				+ cosDeg(this.y) * cosDeg(coord.y) * sinDeg(coord.x - this.x)));
	}

	/**
	 * Gives new instance of a vector with this SpCoord.
	 */
	public Vector3 getVec() {
		return new Vector3(cosDeg(y) * cosDeg(x), cosDeg(y) * sinDeg(x), sinDeg(y));
	}

	private static double sinDeg(double deg) {
		return Math.sin(Math.toRadians(deg));
	}

	private static double cosDeg(double deg) {
		return Math.cos(Math.toRadians(deg));
	}

	/**
	 * Set this SpCoord with vector.
	 * 
	 * @param vec the vector
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
