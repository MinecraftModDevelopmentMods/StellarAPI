package stellarapi.api.position;

import stellarapi.api.lib.math.Matrix3;
import stellarapi.api.lib.math.Vector3;

/**
 * Celestial transformation.
 * Currently, it should be either periodic or constant, and should be smooth on time.
 * */
public interface ICTransform {
	/**
	 * Gets the period.
	 * If it has no period, gives {@link Double#POSITIVE_INFINITY}.
	 * */
	public double getPeriod(Vector3 position);

	/**
	 * Transforms the position for the given time.
	 * */
	public Vector3 transform(Vector3 position, double time);

	/**
	 * Gets the actual transformation.
	 * */
	public Matrix3 getTransformation();
}
