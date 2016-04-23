package stellarapi.api.lib.math;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

import net.minecraft.util.MathHelper;

public class Spmath {
		
	public static final float PI=(float)Math.PI;
	
	public static final int signi = 60000;
	
	private static final int ATAN2_BITS = 8;

	private static final int ATAN2_BITS2 = ATAN2_BITS << 1;
	private static final int ATAN2_MASK = ~(-1 << ATAN2_BITS2);
	private static final int ATAN2_COUNT = ATAN2_MASK + 1;
	private static final int ATAN2_DIM = (int) Math.sqrt(ATAN2_COUNT);

	private static final float INV_ATAN2_DIM_MINUS_1 = 1.0f / (ATAN2_DIM - 1);
	private static final float DEG = 180.0f / (float) Math.PI;

	private static final float[] atan2 = new float[ATAN2_COUNT];
	
	public static float dattan[];
	public static double datasin[];
	
	//Angle Undercut
	public static final double AngleUndercut(double x){
		if(x<0) return x+2.0*Math.PI;
		return x;
	}
	
	//fmod
	public static final double fmod(double a, double b){
		return a-Math.floor(a/b)*b;
	}
	
	//fmod
	public static final float fmod(float a, float b){
		return a-(float)Math.floor(a/b)*b;
	}
	
	//Degrees to Radians
	public static final double Radians(double d){
		return d*Math.PI/180.0;
	}
	
	//Radians to Degrees
	public static final double Degrees(double r){
		return r/Math.PI*180.0;
	}
	
	//Degrees to Radians
	public static final float Radians(float d){
		return d*PI/180.0f;
	}
	
	//Radians to Degrees
	public static final float Degrees(float r){
		return r/PI*180.0f;
	}
	
	//Preparing datas for float sin/cos/tan
	public static final void Initialize(){
		int i;
		dattan=new float[signi+1];
		datasin=new double[signi+1];
		for(i=0; i<=signi; i++){
			dattan[i]=(float)Math.tan((double)i*Math.PI/signi);
			datasin[i]=(float) Math.asin(i*2.0/signi-1);
		}
		
		for (i = 0; i < ATAN2_DIM; i++)
		{
			for (int j = 0; j < ATAN2_DIM; j++)
			{
				float x0 = (float) i / ATAN2_DIM;
				float y0 = (float) j / ATAN2_DIM;

				atan2[j * ATAN2_DIM + i] = (float) Math.atan2(y0, x0);
			}
		}	
	}
	
	//Float sine
	public static final float sinf(float d){
		return MathHelper.sin(d);
	}
	
	//Float cosine
	public static final float cosf(float d){
		return MathHelper.cos(d);
	}
	
	//Float tangent
	public static final float tanf(float d){
		int k=MathHelper.floor_float(d*signi/PI);
		k%=signi;
		if(k<0) k+=signi;
		return dattan[k];
	}
	
	public static final double asin(double d) {
		int k = MathHelper.floor_double((d+1)*signi/2);
		if(k < 0 || k > signi)
			return Float.NaN;
		return datasin[k];
	}
	
	public static final double atan2(double d, double e)
	{
		double add, mul;

		if (e < 0.0f)
		{
			if (d < 0.0f)
			{
				e = -e;
				d = -d;

				mul = 1.0f;
			}
			else
			{
				e = -e;
				mul = -1.0f;
			}

			add = -3.141592653f;
		}
		else
		{
			if (d < 0.0f)
			{
				d = -d;
				mul = -1.0f;
			}
			else
			{
				mul = 1.0f;
			}

			add = 0.0f;
		}

		double invDiv = 1.0 / (((e < d) ? d : e) * INV_ATAN2_DIM_MINUS_1);

		int xi = (int) (e * invDiv);
		int yi = (int) (d * invDiv);
		double part = e * invDiv - xi;
		double part2 = d * invDiv - yi;

		return (atan2[yi * ATAN2_DIM + xi] * (1-part) * (1-part2)
				+ atan2[yi * ATAN2_DIM + xi + 1] * part * (1-part2)
				+ atan2[(yi + 1) * ATAN2_DIM + xi] * (1-part) * part2
				+ atan2[(yi + 1) * ATAN2_DIM + xi + 1] * part * part2
				+ add) * mul;
	}
	
	//Degree sine
	public static final double sind(double d){
		return Math.sin(Radians(d));
	}
	
	//Degree cosine
	public static final double cosd(double d){
		return Math.cos(Radians(d));
	}
	
	//Degree tangent
	public static final double tand(double d){
		return Math.tan(Radians(d));
	}
	
	//Degree sine
	public static final float sind(float d){
		return sinf(Radians(d));
	}
	
	//Degree cosine
	public static final float cosd(float d){
		return cosf(Radians(d));
	}
	
	//Degree tangent
	public static final float tand(float d){
		return tanf(Radians(d));
	}
	
	/*public static double magToLum(double Mag){
		return Math.pow(10.0, (-26.74) - Mag/2.5);
	}*/

	public static double sqr(double d) {
		return d*d;
	}
	
	public static double quad(double d) {
		return d*d*d*d;
	}
}