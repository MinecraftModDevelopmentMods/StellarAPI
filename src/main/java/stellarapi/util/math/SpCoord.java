package stellarapi.util.math;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

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

	/**Gives Vector with this SpCoord*/
	public Vector3d getVec(){
		return new Vector3d(Spmath.cosd(y)*Spmath.cosd(x), Spmath.cosd(y)*Spmath.sind(x), Spmath.sind(y));
	}
	
	/**Set this SpCoord with vector.*/
	public void setWithVec(Vector3d vec){
		Vector3d temp = new Vector3d();
		vec.normalize(temp);
		x = Spmath.Degrees(Spmath.atan2(temp.y, temp.x));
		y = Spmath.Degrees(Spmath.asin(temp.z));
	}
}
