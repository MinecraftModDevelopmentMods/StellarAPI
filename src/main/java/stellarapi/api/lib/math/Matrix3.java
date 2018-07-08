package stellarapi.api.lib.math;

/**
 * 3*3 Mutable Matrix for double.
 */
public class Matrix3 {

	private static final int DIM = 3;

	private double value[][] = new double[DIM][DIM];

	/**
	 * Creates a Matrix as Zero Matrix.
	 */
	public Matrix3() {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] = 0.0;
	}

	public Matrix3(Matrix3 ref) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] = ref.value[i][j];
	}

	public Matrix3(double... val) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] = val[i * DIM + j];
	}

	public Matrix3 set(Matrix3 val) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] = val.value[i][j];

		return this;
	}

	public Matrix3 set(double... val) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] = val[i * DIM + j];
		return this;
	}

	public Matrix3 setIdentity() {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] = (i == j) ? 1.0 : 0.0;

		return this;
	}

	public Matrix3 setRow(int N, Vector3 val) {
		for (int j = 0; j < DIM; j++)
			value[N][j] = val.getCoord(j);
		return this;
	}

	public Matrix3 setColumn(int N, Vector3 val) {
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

	public Matrix3 add(Matrix3 val) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] += val.value[i][j];
		return this;
	}

	public Matrix3 setAdd(Matrix3 par1, Matrix3 par2) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] = par1.value[i][j] + par2.value[i][j];
		return this;
	}

	public Matrix3 sub(Matrix3 val) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] -= val.value[i][j];
		return this;
	}

	public Matrix3 setSub(Matrix3 par1, Matrix3 par2) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] = par1.value[i][j] - par2.value[i][j];
		return this;
	}

	public Matrix3 scale(double scale) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				value[i][j] *= scale;
		return this;
	}

	public Matrix3 preMult(Matrix3 par1) {
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

	public Matrix3 postMult(Matrix3 par2) {
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

	public Matrix3 setMult(Matrix3 par1, Matrix3 par2) {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++) {
				value[i][j] = 0.0;

				for (int k = 0; k < DIM; k++)
					value[i][j] += par1.value[i][k] * par2.value[k][j];
			}

		return this;
	}

	public Matrix3 transpose() {
		double tmp;
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < i; j++) {
				tmp = value[i][j];
				value[i][j] = value[j][i];
				value[j][i] = tmp;
			}
		return this;
	}

	// TODO Math Fix this to avoid license infringement
	public Matrix3 invert() {
		double result[] = new double[9];
		int row_perm[] = new int[3];
		int i;
		double[] tmp = new double[9];  // scratch matrix

		// Use LU decomposition and backsubstitution code specifically
		// for floating-point 3x3 matrices.

		// Copy source matrix to t1tmp
		tmp[0] = this.value[0][0];
		tmp[1] = this.value[0][1];
		tmp[2] = this.value[0][2];

		tmp[3] = this.value[1][0];
		tmp[4] = this.value[1][1];
		tmp[5] = this.value[1][2];

		tmp[6] = this.value[2][0];
		tmp[7] = this.value[2][1];
		tmp[8] = this.value[2][2];


		// Calculate LU decomposition: Is the matrix singular?
		if (!luDecomposition(tmp, row_perm)) {
			// Matrix has no inverse
			throw new RuntimeException("Not invertible");
		}

		// Perform back substitution on the identity matrix
		for(i=0;i<9;i++) result[i] = 0.0;
		result[0] = 1.0; result[4] = 1.0; result[8] = 1.0;
		luBacksubstitution(tmp, row_perm, result);

		this.value[0][0] = result[0];
		this.value[0][1] = result[1];
		this.value[0][2] = result[2];

		this.value[1][0] = result[3];
		this.value[1][1] = result[4];
		this.value[1][2] = result[5];

		this.value[2][0] = result[6];
		this.value[2][1] = result[7];
		this.value[2][2] = result[8];

		return this;
	}

	static boolean luDecomposition(double[] matrix0,
			int[] row_perm) {

		double row_scale[] = new double[3];

		// Determine implicit scaling information by looping over rows
		{
			int i, j;
			int ptr, rs;
			double big, temp;

			ptr = 0;
			rs = 0;

			// For each row ...
			i = 3;
			while (i-- != 0) {
				big = 0.0;

				// For each column, find the largest element in the row
				j = 3;
				while (j-- != 0) {
					temp = matrix0[ptr++];
					temp = Math.abs(temp);
					if (temp > big) {
						big = temp;
					}
				}

				// Is the matrix singular?
				if (big == 0.0) {
					return false;
				}
				row_scale[rs++] = 1.0 / big;
			}
		}

		{
			int j;
			int mtx;

			mtx = 0;

			// For all columns, execute Crout's method
			for (j = 0; j < 3; j++) {
				int i, imax, k;
				int target, p1, p2;
				double sum, big, temp;

				// Determine elements of upper diagonal matrix U
				for (i = 0; i < j; i++) {
					target = mtx + (3*i) + j;
					sum = matrix0[target];
					k = i;
					p1 = mtx + (3*i);
					p2 = mtx + j;
					while (k-- != 0) {
						sum -= matrix0[p1] * matrix0[p2];
						p1++;
						p2 += 3;
					}
					matrix0[target] = sum;
				}

				// Search for largest pivot element and calculate
				// intermediate elements of lower diagonal matrix L.
				big = 0.0;
				imax = -1;
				for (i = j; i < 3; i++) {
					target = mtx + (3*i) + j;
					sum = matrix0[target];
					k = j;
					p1 = mtx + (3*i);
					p2 = mtx + j;
					while (k-- != 0) {
						sum -= matrix0[p1] * matrix0[p2];
						p1++;
						p2 += 3;
					}
					matrix0[target] = sum;

					// Is this the best pivot so far?
					if ((temp = row_scale[i] * Math.abs(sum)) >= big) {
						big = temp;
						imax = i;
					}
				}

				if (imax < 0) {
					throw new RuntimeException("Fail");
				}

				// Is a row exchange necessary?
				if (j != imax) {
					// Yes: exchange rows
					k = 3;
					p1 = mtx + (3*imax);
					p2 = mtx + (3*j);
					while (k-- != 0) {
						temp = matrix0[p1];
						matrix0[p1++] = matrix0[p2];
						matrix0[p2++] = temp;
					}

					// Record change in scale factor
					row_scale[imax] = row_scale[j];
				}

				// Record row permutation
				row_perm[j] = imax;

				// Is the matrix singular
				if (matrix0[(mtx + (3*j) + j)] == 0.0) {
					return false;
				}

				// Divide elements of lower diagonal matrix L by pivot
				if (j != (3-1)) {
					temp = 1.0 / (matrix0[(mtx + (3*j) + j)]);
					target = mtx + (3*(j+1)) + j;
					i = 2 - j;
					while (i-- != 0) {
						matrix0[target] *= temp;
						target += 3;
					}
				}
			}
		}

		return true;
	}

	static void luBacksubstitution(double[] matrix1,
			int[] row_perm,
			double[] matrix2) {

		int i, ii, ip, j, k;
		int rp;
		int cv, rv;

		//	rp = row_perm;
		rp = 0;

		// For each column vector of matrix2 ...
		for (k = 0; k < 3; k++) {
			//	    cv = &(matrix2[0][k]);
			cv = k;
			ii = -1;

			// Forward substitution
			for (i = 0; i < 3; i++) {
				double sum;

				ip = row_perm[rp+i];
				sum = matrix2[cv+3*ip];
				matrix2[cv+3*ip] = matrix2[cv+3*i];
				if (ii >= 0) {
					//		    rv = &(matrix1[i][0]);
					rv = i*3;
					for (j = ii; j <= i-1; j++) {
						sum -= matrix1[rv+j] * matrix2[cv+3*j];
					}
				}
				else if (sum != 0.0) {
					ii = i;
				}
				matrix2[cv+3*i] = sum;
			}

			// Backsubstitution
			//	    rv = &(matrix1[3][0]);
			rv = 2*3;
			matrix2[cv+3*2] /= matrix1[rv+2];

			rv -= 3;
			matrix2[cv+3*1] = (matrix2[cv+3*1] -
					matrix1[rv+2] * matrix2[cv+3*2]) / matrix1[rv+1];

			rv -= 3;
			matrix2[cv+4*0] = (matrix2[cv+3*0] -
					matrix1[rv+1] * matrix2[cv+3*1] -
					matrix1[rv+2] * matrix2[cv+3*2]) / matrix1[rv+0];

		}
	}

	/** Transform a vector and return the vector. */
	public Vector3 transform(Vector3 vec) {
		double[] cache = new double[DIM];

		for (int i = 0; i < DIM; i++) {
			cache[i] = 0.0;
			for (int k = 0; k < DIM; k++)
				cache[i] = cache[i] + value[i][k] * vec.getCoord(k);
		}

		return vec.set(cache);
	}

	public Matrix3 setAsRotation(double x, double y, double z, double angle) {
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

	public Matrix3 setAsRotation(Vector3 axis, double angle) {
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
