package stellarapi.api.celestials;

import java.util.Set;

import stellarapi.api.math.SpCoord;

/**
 * Interface representing certain celestial collection, i.e. celestial layer.
 * */
public interface ICelestialCollection {
	
	/**
	 * Name of this celestial collection.
	 * */
	public String getName();
	
	/**
	 * Gets the set of celestial objects.
	 * */
	public Set<ICelestialObject> getObjects();

	/**
	 * Gets the set of celestial objects in certain range.
	 * @param pos the horizontal spherical position of the center of the range
	 * @param radius the angular radius of the range in degrees
	 * */
	public Set<ICelestialObject> getObjectInRange(SpCoord pos, double radius);
	
	/**
	 * Gets the nearer celestial object to certain position from two objects.
	 * @param pos the position
	 * @param obj1 one of the two objects
	 * @param obj2 another object of the two object
	 * @return celestial object which is nearer to the position
	 * */
	public ICelestialObject getNearerObject(SpCoord pos, ICelestialObject obj1, ICelestialObject obj2);
	
	
	/**
	 * Gets the default search order of this collection. <p>
	 * The higher the order value is, the latter this collection gets found. <p>
	 * Should be constant.
	 * */
	public int searchOrder();
	
	/**
	 * Gets flag if this collection should be served as background,
	 *  i.e. the objects are fixed on the celestial sphere, without any change.
	 * */
	public boolean isBackground();
	
	/**
	 * Gets type of this collection.
	 * */
	public EnumCelestialCollectionType getCollectionType();

}
