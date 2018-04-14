package stellarapi.api.coordinates;

import java.util.List;

import stellarapi.api.lib.math.Matrix3;
import stellarapi.api.position.ICTransform;

public interface ICoordinatesInstance {
	// Is this right to always count from horizontal coordinates?
	/** Gets the list of transforms from horizontal coordinates in applied order. */
	public List<ICTransform> getTransforms();

	/** Gets current transform matrix from horizontal coordinates. */
	public Matrix3 getTransformMatrix();

	/** Updates this coordinates instance. */
	public void update(CoordContext context);
}
