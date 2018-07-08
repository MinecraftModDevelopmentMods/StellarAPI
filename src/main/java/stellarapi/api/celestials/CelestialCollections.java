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

	private List<CelestialCollection> celestialCollections;

	public CelestialCollections(List<CelestialCollection> collections) {
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
	public Set<CelestialObject> findIn(SearchRegion region, float efficiency, float multPower) {
		Set<CelestialObject> foundSet = Sets.newHashSet();

		for (CelestialCollection collection : this.celestialCollections) {
			Set<? extends CelestialObject> objectSet = collection.findIn(region, efficiency, multPower);
			foundSet.addAll(objectSet);
		}

		return foundSet;
	}

}
