package stellarapi.api.celestials;

import java.util.Set;
import java.util.stream.Collectors;

import stellarapi.api.observe.SearchRegion;

public abstract class AbstractCelestialCollection implements ICelestialCollection {
	private final String name;
	private final Set<ICelestialObject> objects;
	private final int searchOrder;
	private final EnumCollectionType type;

	public AbstractCelestialCollection(String nameIn, Set<ICelestialObject> objectsIn, int searchOrderIn, EnumCollectionType typeIn) {
		this.name = nameIn;
		this.objects = objectsIn;
		this.searchOrder = searchOrderIn;
		this.type = typeIn;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Set<ICelestialObject> findIn(SearchRegion region, float efficiency, float multPower) {
		return objects.stream()
				.filter(object -> this.isIn(object, region))
				.filter(object -> this.IsVisible(object, efficiency, multPower))
				.collect(Collectors.toSet());
	}

	@Override
	public int searchOrder() {
		return this.searchOrder;
	}

	@Override
	public EnumCollectionType getCollectionType() {
		return this.type;
	}

	public abstract boolean isIn(ICelestialObject object, SearchRegion region);
	public abstract boolean IsVisible(ICelestialObject object, float efficiency, float multPower);
}
