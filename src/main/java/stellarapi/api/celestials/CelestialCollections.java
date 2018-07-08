package stellarapi.api.celestials;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import stellarapi.api.observe.SearchRegion;

/**
 * Container for celestial collections.
 * <p>
 * Provides useful helper methods related with celestial collection.
 */
public final class CelestialCollections {

	private List<ICelestialCollection> celestialCollections;

	public CelestialCollections(List<ICelestialCollection> collections) {
		this.celestialCollections = collections;
	}

	/**
	 * Finds all visible celestial objects in certain region.
	 * 
	 * @param region the search region in absolute coordinates
	 * @param multPower multiplying power of the viewer
	 * @param efficiency quantum efficiency of the viewer
	 * @return all objects in the search range which is visible
	 */
	public Set<ICelestialObject> findIn(SearchRegion region, float efficiency, float multPower) {
		Set<ICelestialObject> foundSet = Sets.newHashSet();

		for (ICelestialCollection collection : this.celestialCollections) {
			Set<? extends ICelestialObject> objectSet = collection.findIn(region, efficiency, multPower);
			foundSet.addAll(objectSet);
		}

		return foundSet;
	}

}
