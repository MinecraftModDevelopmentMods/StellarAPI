package stellarapi.api.celestials;

import com.google.common.collect.ImmutableSet;

import stellarapi.api.coordinates.CCoordinates;
import stellarapi.api.lib.math.SpCoord;

/**
 * Interface representing certain celestial collection, i.e. celestial layer.
 */
public interface ICelestialCollection {

	/**
	 * Name of this celestial collection.
	 */
	public String getName();

	/**
	 * Gets the set of celestial objects.
	 * 
	 * @return the set of celestial objects contained in the collection, or
	 *         empty set if it is not available. (Especially when the set is too
	 *         big)
	 */
	public ImmutableSet<? extends ICelestialObject> getObjects();

	/**
	 * Gets the set of celestial objects in certain range.
	 * <p>
	 * More recommended to use this version, since there can be objects without
	 * positions.
	 * <p>
	 * Note that resulted objects can be temporal, i.e. it doesn't get updated
	 * anymore.
	 * 
	 * @param pos
	 *            the horizontal spherical position of the center of the range
	 * @param radius
	 *            the angular radius of the range in degrees
	 */
	public ImmutableSet<? extends ICelestialObject> getObjectInRange(SpCoord pos, double radius);

	/**
	 * Gets the nearer celestial object to certain position from two objects.
	 * 
	 * @param pos
	 *            the position
	 * @param obj1
	 *            one of the two objects
	 * @param obj2
	 *            another object of the two object
	 * @return celestial object which is nearer to the position
	 */
	public ICelestialObject getNearerObject(SpCoord pos, ICelestialObject obj1, ICelestialObject obj2);

	/**
	 * Gets the default search order of this collection.
	 * <p>
	 * The higher the order value is, the latter this collection gets found.
	 * <p>
	 * Should be constant.
	 */
	public int searchOrder();

	/**
	 * Gets the base coordinates for this collection.
	 * */
	public CCoordinates baseCoordinates();

	/**
	 * Gets type of this collection.
	 */
	public EnumCelestialCollectionType getCollectionType();

}
