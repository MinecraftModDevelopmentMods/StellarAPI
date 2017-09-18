package stellarapi.api.coordinates;

import stellarapi.api.lib.math.Matrix4;

/**
 * Generic capability of local coordinates for various (positioned) objects.
 * This effectively transforms context and fetches the matrix for the coordinates.
 * Note: World should have position-specific defaults.
 * */
public interface ILocalCoordinates {
	/**
	 * Gets transform matrix.
	 * Transforms context in appropriate way.
	 * */
	public Matrix4 getTransformMatrix(CCoordinates coord, CoordContext baseContext);

	/**
	 * Gets interpolated transform matrix.
	 * Transforms context in appropriate way.
	 * Offset of 1 is applied on time to evaluate the post-state.
	 * */
	public Matrix4 getTransformMatrix(CCoordinates coord, CoordContext baseContext, float partialTicks);
}