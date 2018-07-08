package stellarapi.api.celestials;

import java.util.Set;

import stellarapi.api.observe.SearchRegion;

/**
 * Interface representing certain celestial collection, i.e. celestial layer.
 */
public interface ICelestialCollection {

	/**
	 * Name of this celestial collection.
	 */
	public String getName();

	/**
	 * Finds all visible celestial objects in certain region.
	 * 
	 * @param region the search region in absolute coordinates
	 * @param multPower multiplying power of the viewer
	 * @param efficiency quantum efficiency of the viewer
	 * @return all objects in the search range which is visible
	 */
	public Set<ICelestialObject> findIn(SearchRegion region, float efficiency, float multPower);

	/**
	 * Gets the default search order of this collection.
	 * <p>
	 * The higher the order value is, the latter this collection gets found.
	 * <p>
	 * Should be constant.
	 */
	public int searchOrder();

	/**
	 * Gets type of this collection.
	 */
	public EnumCollectionType getCollectionType();

}
