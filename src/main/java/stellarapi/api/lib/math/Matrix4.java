package stellarapi.api.lib.math;

/**
 * 3*3 Mutable Matrix for double.
 */
public class Matrix4 {

	private static final int DIM = 3;

	private double value[][] = new double[DIM][DIM];

	/**
	 * Creates a Matrix as Zero Matrix.
	 */
	public Matrix4() {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] = 0.0;
	}

	public Matrix4(Matrix4 ref) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] = ref.value[i][j];
	}

	public Matrix4(double... val) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] = val[i * DIM + j];
	}

	public Matrix4 set(Matrix4 val) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] = val.value[i][j];

		return this;
	}

	public Matrix4 set(double... val) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] = val[i * DIM + j];
		return this;
	}

	public Matrix4 setIdentity() {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] = (i == j) ? 1.0 : 0.0;

		return this;
	}

	public Matrix4 setRow(int N, Vector3 val) {
		for (int j = 0; j < DIM; j++)
			value[N][j] = val.getCoord(j);
		return this;
	}

	public Matrix4 setColumn(int N, Vector3 val) {
		for (int i = 0; i < DIM; i++)
			value[i][N] = val.getCoord(i);
		return this;
	}

	public double getElement(int i, int j) {
		return value[i][j];
	}

	public void setElement(int i, int j, double val) {
		value[i][j] = val;
	}

	public Matrix4 add(Matrix4 val) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] += val.value[i][j];
		return this;
	}

	public Matrix4 setAdd(Matrix4 par1, Matrix4 par2) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] = par1.value[i][j] + par2.value[i][j];
		return this;
	}

	public Matrix4 sub(Matrix4 val) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] -= val.value[i][j];
		return this;
	}

	public Matrix4 setSub(Matrix4 par1, Matrix4 par2) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] = par1.value[i][j] - par2.value[i][j];
		return this;
	}

	public Matrix4 scale(double scale) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] *= scale;
		return this;
	}

	public Matrix4 preMult(Matrix4 par1) {
		double cache[] = new double[DIM * DIM];

		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++) {
				cache[i * DIM + j] = 0.0;

				for (int k = 0; k < DIM; k++)
					cache[i * DIM + j] += par1.value[i][k] * value[k][j];
			}

		this.set(cache);

		return this;
	}

	public Matrix4 postMult(Matrix4 par2) {
		double cache[] = new double[DIM * DIM];

		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++) {
				cache[i * DIM + j] = 0.0;

				for (int k = 0; k < DIM; k++)
					cache[i * DIM + j] += value[i][k] * par2.value[k][j];
			}

		this.set(cache);

		return this;
	}

	public Matrix4 setMult(Matrix4 par1, Matrix4 par2) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++) {
				value[i][j] = 0.0;

				for (int k = 0; k < DIM; k++)
					value[i][j] += par1.value[i][k] * par2.value[k][j];
			}

		return this;
	}

	public Matrix4 transpose() {
		double tmp;
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < i; j++) {
				tmp = value[i][j];
				value[i][j] = value[j][i];
				value[j][i] = tmp;
			}
		return this;
	}

	public Vector3 transform(Vector3 vec) {
		double[] cache = new double[DIM];

		for (int i = 0; i < DIM; i++) {
			cache[i] = 0.0;
			for (int k = 0; k < DIM; k++)
				cache[i] = cache[i] + value[i][k] * vec.getCoord(k);
		}

		return vec.set(cache);
	}

	public Matrix4 setAsRotation(double x, double y, double z, double angle) {
		double mag = 1 / Math.sqrt(x * x + y * y + z * z);
		double ax = x * mag;
		double ay = y * mag;
		double az = z * mag;

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

	public Matrix4 setAsRotation(Vector3 axis, double angle) {
		return this.setAsRotation(axis.getX(), axis.getY(), axis.getZ(), angle);
	}

	@Override
	public String toString() {
		String p = "(";
		for (double[] ite : this.value) {
			for (double ite2 : ite) {
				p += String.valueOf(ite2);
				p += ",";
			}
			p += "\n";
		}
		return p + ")";
	}
}
