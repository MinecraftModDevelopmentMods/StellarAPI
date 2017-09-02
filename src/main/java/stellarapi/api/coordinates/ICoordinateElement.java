package stellarapi.api.coordinates;

import java.util.EnumSet;

import stellarapi.api.lib.math.Matrix4;
import stellarapi.api.lib.math.Vector4;

public interface ICoordinateElement {
	/**
	 * Creates new instance of transformation Matrix.
	 * */
	public Matrix4 transformMatrix(CoordContext context);

	/**
	 * Creates new instance of inverse transformation Matrix.
	 * */
	public Matrix4 inverseTransformMatrix(CoordContext context);

	/**
	 * Transforms the vector and returns the instance itself.
	 * Create new vector if you want. (premult standard)
	 * */
	public Vector4 transform(CoordContext context, Vector4 instance);

	/**
	 * Inverse-transforms the vector and returns the instance itself.
	 * Create new vector if you want. (premult standard)
	 * */
	public Vector4 inverseTransform(CoordContext context, Vector4 instance);

	/**
	 * Gives set of required context types.
	 * */
	public EnumSet<EnumContextType> requiredContextTypes();
}
