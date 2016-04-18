package stellarapi.api.celestials;

import java.util.List;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import stellarapi.api.lib.math.SpCoord;

/**
 * Container for celestial collections. <p>
 * Provides useful helper methods related with celestial collection.
 * */
public final class CelestialCollectionManager {
	
	private List<ICelestialCollection> celestialCollections;
	
	public CelestialCollectionManager(List<ICelestialCollection> collections) {
		this.celestialCollections = collections;
	}
	
	/**
	 * Finds the celestial object in certain range near certain position. <p>
	 * Note that in this method the default ordering of collection is prior to the distance to the position.
	 * @param pos the horizontal spherical position
	 * @param radius the angular radius of the search range
	 * @return the object near the position in the range, or <code>null</code> if there is no celestial object within the range
	 * */
	public ICelestialObject findObjectInRange(SpCoord pos, double radius) {
		return this.findObjectInRange(pos, radius,
				Predicates.<ICelestialCollection>alwaysTrue(),
				Predicates.<ICelestialObject>alwaysTrue());
	}
	
	/**
	 * Finds the celestial object in certain range near certain position. <p>
	 * Note that in this method the default ordering of collection is prior to the distance to the position.
	 * @param pos the horizontal spherical position
	 * @param radius the angular radius of the search range
	 * @param collectionChecker the checker to filter collections of objects
	 * @param objectChecker the checker to filter objects
	 * @return the object near the position in the range, or <code>null</code> if there is no celestial object within the range
	 * */
	public ICelestialObject findObjectInRange(SpCoord pos, double radius,
			Predicate<ICelestialCollection> collectionChecker, Predicate<ICelestialObject> objectChecker) {
		for(ICelestialCollection collection : this.celestialCollections) {
			if(!collectionChecker.apply(collection))
				continue;
			
			Set<ICelestialObject> objectSet = collection.getObjectInRange(pos, radius);
			ICelestialObject foundObject = null;
			
			for(ICelestialObject object : objectSet)
				if(objectChecker.apply(object)) {
					if(foundObject == null)
						foundObject = object;
					else foundObject = collection.getNearerObject(pos, foundObject, object);
				}
			
			if(foundObject != null)
				return foundObject;
		}
		
		return null;
	}
	
	/**
	 * Finds the celestial object in certain range near certain position. <p>
	 * Note that in this method the ordering of collection is prior to the distance to the position. <p>
	 * Same objects in given ordering will have checked in default order.
	 * @param pos the horizontal spherical position
	 * @param radius the angular radius of the search range
	 * @param ordering the ordering to determine the order of collections
	 * @param collectionChecker the checker to filter collections of objects
	 * @param objectChecker the checker to filter objects
	 * @return the object near the position in the range, or <code>null</code> if there is no celestial object within the range
	 * */
	public ICelestialObject findObjectInRange(SpCoord pos, double radius,
			Ordering<ICelestialCollection> ordering,
			Predicate<ICelestialCollection> collectionChecker, Predicate<ICelestialObject> objectChecker) {
		for(ICelestialCollection collection : ordering.immutableSortedCopy(this.celestialCollections)) {
			if(!collectionChecker.apply(collection))
				continue;
			
			Set<ICelestialObject> objectSet = collection.getObjectInRange(pos, radius);
			ICelestialObject foundObject = null;
			
			for(ICelestialObject object : objectSet)
				if(objectChecker.apply(object)) {
					if(foundObject == null)
						foundObject = object;
					else foundObject = collection.getNearerObject(pos, foundObject, object);
				}
			
			if(foundObject != null)
				return foundObject;
		}
		
		return null;
	}
	
	/**
	 * Finds all celestial objects in certain range.
	 * @param pos the horizontal spherical position of the center of the search range
	 * @param radius the angular radius of the search range
	 * @return all objects in the search range
	 * */
	public Set<ICelestialObject> findAllObjectsInRange(SpCoord pos, double radius) {
		return this.findAllObjectsInRange(pos, radius, Predicates.<ICelestialCollection>alwaysTrue());
	}
	
	/**
	 * Finds all celestial objects in certain range.
	 * @param pos the horizontal spherical position of the center of the search range
	 * @param radius the angular radius of the search range
	 * @param checker the checker to filter collections of objects
	 * @return all objects in the search range
	 * */
	public Set<ICelestialObject> findAllObjectsInRange(SpCoord pos, double radius, Predicate<ICelestialCollection> checker) {
		Set<ICelestialObject> foundSet = Sets.newHashSet();
		
		for(ICelestialCollection collection : this.celestialCollections) {
			if(checker.apply(collection)) {
				Set<ICelestialObject> objectSet = collection.getObjectInRange(pos, radius);
				foundSet.addAll(objectSet);
			}
		}
		
		return foundSet;
	}
	
}
