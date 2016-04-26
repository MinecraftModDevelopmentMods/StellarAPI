package stellarapi.api.lib.math;

/**
 * 3*3 Mutable Matrix for double.
 * */
public class Matrix3 {
	
	private static final int DIM = 3;
		
	private double value[][] = new double[DIM][DIM];
	
	/**
	 * Creates a Matrix as Zero Matrix.
	 * */
	public Matrix3() {
		for(int i = 0; i < DIM; i++)
			for(int j = 0; j < DIM; j++)
				value[i][j] = 0.0;
	}

	public Matrix3 set(Matrix3 val) {
		for(int i = 0; i < DIM; i++)
			for(int j = 0; j < DIM; j++)
				value[i][j] = val.value[i][j];
		
		return this;
	}

	public double getElement(int i, int j) {
		return value[i][j];
	}
	
	public void setElement(int i, int j, double val) {
		value[i][j] = val;
	}
	
	public Matrix3 add(Matrix3 val) {
		for(int i = 0; i < DIM; i++)
			for(int j = 0; j < DIM; j++)
				value[i][j] += val.value[i][j];
		return this;
	}
	
	public Matrix3 setAdd(Matrix3 par1, Matrix3 par2) {
		for(int i = 0; i < DIM; i++)
			for(int j = 0; j < DIM; j++)
				value[i][j] = par1.value[i][j] + par2.value[i][j];
		return this;
	}
	
	public Matrix3 sub(Matrix3 val) {
		for(int i = 0; i < DIM; i++)
			for(int j = 0; j < DIM; j++)
				value[i][j] -= val.value[i][j];
		return this;
	}
	
	public Matrix3 setSub(Matrix3 par1, Matrix3 par2) {
		for(int i = 0; i < DIM; i++)
			for(int j = 0; j < DIM; j++)
				value[i][j] = par1.value[i][j] - par2.value[i][j];
		return this;
	}
	
	public Matrix3 scale(double scale) {
		for(int i = 0; i < DIM; i++)
			for(int j = 0; j < DIM; j++)
				value[i][j] *= scale;
		return this;
	}
	
	public Matrix3 setMult(Matrix3 par1, Matrix3 par2) {
		for(int i = 0; i < DIM; i++)
			for(int j = 0; j < DIM; j++)
			{
				value[i][j] = 0.0;
				
				for(int k = 0; k < DIM; k++)
					value[i][j] += par1.value[i][k] * par2.value[k][j];
			}
		
		return this;
	}
	
	public Matrix3 transpose() {
		double tmp;
		for(int i = 0; i < DIM; i++)
			for(int j = 0; j < i; j++)
			{
				tmp = value[i][j];
				value[i][j] = value[j][i];
				value[j][i] = tmp;
			}
		return this;
	}
	
	public Vector3 transform(Vector3 vec) {
		double[] cache = new double[DIM];

		for(int i = 0; i < DIM; i++)
		{
			cache[i] = 0.0;
			for(int k = 0; k < DIM; k++)
				cache[i] = cache[i] + value[i][k] * vec.getCoord(k);
		}
			
		return vec.set(cache);
	}
	
	public Matrix3 setAsRotation(double x, double y, double z, double angle) {
		double mag = 1 / Math.sqrt(x*x+y*y+z*z);
        double ax = x*mag;
        double ay = y*mag;
        double az = z*mag;

        double sinTheta = Math.sin(angle);
        double cosTheta = Math.cos(angle);
        double t = 1.0 - cosTheta;

        double xz = ax * az;
        double xy = ax * ay;
        double yz = ay * az;

        value[0][0] = t * ax * ax + cosTheta;
        value[0][1] = t * xy - sinTheta * az;
        value[0][2] = t * xz + sinTheta * ay;

        value[1][0] = t * xy + sinTheta * az;
        value[1][1] = t * ay * ay + cosTheta;
        value[1][2] = t * yz - sinTheta * ax;

        value[2][0] = t * xz - sinTheta * ay;
        value[2][1] = t * yz + sinTheta * ax;
        value[2][2] = t * az * az + cosTheta;
        
        return this;
	}
	
	public Matrix3 setAsRotation(Vector3 axis, double angle) {
		return this.setAsRotation(axis.getX(), axis.getY(), axis.getZ(), angle);
	}
}
