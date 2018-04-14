package stellarapi.api.coordinates;

import java.util.List;

import stellarapi.api.lib.math.Matrix3;
import stellarapi.api.position.ICTransform;

/** A coordinates element implementation. */
public interface ICoordElementImpl extends ICTransform {
	/** Updates this coordinates instance. */
	public void update(CoordContext context);
}
