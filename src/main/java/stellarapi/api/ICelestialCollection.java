package stellarapi.api;

import java.util.Set;

import stellarapi.util.math.SpCoord;

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

}
