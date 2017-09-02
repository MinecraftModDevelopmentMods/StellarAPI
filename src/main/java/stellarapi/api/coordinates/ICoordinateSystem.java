package stellarapi.api.coordinates;

import java.util.Map;

import stellarapi.api.lib.math.Matrix4;

public interface ICoordinateSystem {

	/** Evaluates the elements from the ground to the specified coordinates. */
	public Iterable<ICoordinateElement> getElements(CCoordinates coordinates);

	/** Checks for common ancestor. */
	public CCoordinates commonAncestor(CCoordinates coordA, CCoordinates coordB);

	/** Checks the ancestor-descendant relationships. */
	public boolean isDescendant(CCoordinates ancestor, CCoordinates descendant);

	/**
	 * Evaluates the elements between the ancestor and the descendent. <p>
	 * If those are not in the relationship,
	 *  (i.e. not {@link #isDescendant(CCoordinates, CCoordinates) isDescendant},)
	 *  this method gives null.
	 * */
	public Iterable<ICoordinateElement> between(CCoordinates ancestor, CCoordinates descendant);

	/**
	 * Evaluates all the matrices for each coordinates.
	 * */
	public Map<CCoordinates, Matrix4> evaluateAll(CoordContext context, boolean timeOffset1);
}