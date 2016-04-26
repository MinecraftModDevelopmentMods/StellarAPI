package stellarapi.api.lib.math;

//Right Ascension(RA) and Declination(Dec)
//-Azimuth and Height
public class SpCoord {
	/**RA or -Azimuth*/
	public double x;
	/**Dec or Height*/
	public double y;
	
	public SpCoord(double a, double b){
		x=a; y=b;
	}
	
	public SpCoord() {
		this(0.0, 0.0);
	}
	
	/**
	 * Distance to certain SpCoord, note that here not cached version of trigonometric methods are used.
	 * @param coord the coordinate
	 * @return the distance between two coordinates in degrees
	 * */
	public double distanceTo(SpCoord coord) {
		return Spmath.Degrees(Math.acos(Math.sin(this.y) * Math.sin(coord.y)
				+ Math.cos(this.y) * Math.cos(coord.y) * Math.sin(coord.x - this.x)));
	}

	/**
	 * Gives Vector with this SpCoord.
	 * */
	public Vector3 getVec(){
		return new Vector3(Spmath.cosd(y)*Spmath.cosd(x), Spmath.cosd(y)*Spmath.sind(x), Spmath.sind(y));
	}
	
	/**
	 * Set this SpCoord with vector.
	 * @param vec the vector
	 * */
	public void setWithVec(Vector3 vec){
		Vector3 temp = new Vector3(vec);
		temp.normalize();
		x = Spmath.Degrees(Spmath.atan2(temp.getY(), temp.getX()));
		y = Spmath.Degrees(Spmath.asin(temp.getZ()));
	}
}
