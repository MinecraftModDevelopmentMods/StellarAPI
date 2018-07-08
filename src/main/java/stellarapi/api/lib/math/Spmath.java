package stellarapi.api.lib.math;

import net.minecraft.util.math.MathHelper;

public class Spmath {

	public static final float PI = (float) Math.PI;
	public static final double epsilon = 1.0e-12;

	public static final int signi = 60000;

	private static final int ATAN2_BITS = 8;

	private static final int ATAN2_BITS2 = ATAN2_BITS << 1;
	private static final int ATAN2_MASK = ~(-1 << ATAN2_BITS2);
	private static final int ATAN2_COUNT = ATAN2_MASK + 1;
	private static final int ATAN2_DIM = (int) Math.sqrt(ATAN2_COUNT);

	private static final float INV_ATAN2_DIM_MINUS_1 = 1.0f / (ATAN2_DIM - 1);

	private static final float[] atan2 = new float[ATAN2_COUNT];

	public static float dattan[];
	public static double datasin[];

	// Angle Undercut
	public static final double AngleUndercut(double x) {
		if (x < 0)
			return x + 2.0 * Math.PI;
		return x;
	}

	// Preparing datas for float sin/cos/tan
	public static final void Initialize() {
		int i;
		dattan = new float[signi + 1];
		datasin = new double[signi + 1];
		for (i = 0; i <= signi; i++) {
			dattan[i] = (float) Math.tan((double) i * Math.PI / signi);
			datasin[i] = (float) Math.asin(i * 2.0 / signi - 1);
		}

		for (i = 0; i < ATAN2_DIM; i++) {
			for (int j = 0; j < ATAN2_DIM; j++) {
				float x0 = (float) i / ATAN2_DIM;
				float y0 = (float) j / ATAN2_DIM;

				atan2[j * ATAN2_DIM + i] = (float) Math.atan2(y0, x0);
			}
		}
	}

	// TODO Math Correct these
	// Float sine with lookup table
	/*public static final float sinf(float d) {
		return MathHelper.sin(d * PI / 180);
	}

	// Float cosine with lookup table
	public static final float cosf(float d) {
		return MathHelper.cos(d * PI / 180);
	}*/

	// Float tangent with lookup table
	public static final float tanf(float d) {
		int k = MathHelper.floor(d * signi / PI);
		k %= signi;
		if (k < 0)
			k += signi;
		return dattan[k];
	}

	public static final double asin(double d) {
		int k = MathHelper.floor((d + 1) * signi / 2);
		if (k < 0 || k > signi)
			return Float.NaN;
		return datasin[k];
	}

	public static final double atan2(double d, double e) {
		double add, mul;

		if (e < 0.0f) {
			if (d < 0.0f) {
				e = -e;
				d = -d;

				mul = 1.0f;
			} else {
				e = -e;
				mul = -1.0f;
			}

			add = -3.141592653f;
		} else {
			if (d < 0.0f) {
				d = -d;
				mul = -1.0f;
			} else {
				mul = 1.0f;
			}

			add = 0.0f;
		}

		double invDiv = 1.0 / (((e < d) ? d : e) * INV_ATAN2_DIM_MINUS_1);

		int xi = (int) (e * invDiv);
		int yi = (int) (d * invDiv);
		double part = e * invDiv - xi;
		double part2 = d * invDiv - yi;

		return (atan2[yi * ATAN2_DIM + xi] * (1 - part) * (1 - part2)
				+ atan2[yi * ATAN2_DIM + xi + 1] * part * (1 - part2)
				+ atan2[(yi + 1) * ATAN2_DIM + xi] * (1 - part) * part2
				+ atan2[(yi + 1) * ATAN2_DIM + xi + 1] * part * part2 + add) * mul;
	}

	/*
	 * public static double magToLum(double Mag){ return Math.pow(10.0, (-26.74)
	 * - Mag/2.5); }
	 */

	public static double sqr(double d) {
		return d * d;
	}

	public static double quad(double d) {
		return d * d * d * d;
	}
}