package stellarapi.api.celestials;

import java.util.Collections;
import java.util.Set;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.observe.SearchRegion;

/**
 * Interface representing certain celestial collection, i.e. celestial layer.
 */
public class CelestialCollection {
	private final ResourceLocation name;
	private final EnumCollectionType type;
	private final int searchOrder;

	public CelestialCollection(ResourceLocation nameIn, EnumCollectionType typeIn, int order) {
		this.name = nameIn;
		this.type = typeIn;
		this.searchOrder = order;
	}

	/**
	 * Name of this celestial collection.
	 */
	public ResourceLocation getName() {
		return this.name;
	}

	/**
	 * Gets type of this collection.
	 */
	public EnumCollectionType getCollectionType() {
		return this.type;
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
		return Collections.emptySet();
	}

	public int searchOrder() {
		return this.searchOrder;
	}

}
