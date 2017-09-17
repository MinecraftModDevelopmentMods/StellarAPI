package stellarapi.api.coordinates;

import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.lib.math.Matrix4;
import worldsets.api.worldset.WorldSet;

/**
 * WorldSet capability of Coordinates system.
 * This manages coordinates and provides operations on coordinates.
 * */
public interface ICoordSystem {

	/**
	 * Sets provider ID. Throws exception if there's no mapping for the specified id.
	 * Also this re-evaluates the internal handler.
	 * */
	@Deprecated
	public void setProviderID(ResourceLocation providerID);

	/**
	 * Gets the provider ID. Can only be null if it's never set.
	 * */
	public @Nullable ResourceLocation getProviderID();

	/**
	 * Gets the coordinate handler. Can be null if it's never set.
	 * */
	public @Nullable ICoordHandler getHandler();

	/**
	 * Sets up this coordiantes system with the unfinished handler.
	 * */
	public void setupPartial();

	/**
	 * Sets up this coordinates system with the internal handler.
	 * */
	public void setupComplete();


	/** Evaluates the elements from the ground to the specified coordinates. */
	public Iterable<ICoordElement> getElements(CCoordinates coordinates);

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
	public Iterable<ICoordElement> between(CCoordinates ancestor, CCoordinates descendant);

	/**
	 * Evaluates all the matrices for each coordinates.
	 * */
	public Map<CCoordinates, Matrix4> evaluateAll(CoordContext context, boolean timeOffset1);
}