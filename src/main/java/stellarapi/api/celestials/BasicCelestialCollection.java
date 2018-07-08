package stellarapi.api.celestials;

import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.observe.SearchRegion;

public abstract class BasicCelestialCollection extends CelestialCollection {
	private final Set<CelestialObject> objects;

	public BasicCelestialCollection(ResourceLocation nameIn, EnumCollectionType typeIn,
			int searchOrder, Set<CelestialObject> objectsIn) {
		super(nameIn, typeIn, searchOrder);
		this.objects = objectsIn;
	}

	@Override
	public Set<CelestialObject> findIn(SearchRegion region, float efficiency, float multPower) {
		return objects.stream()
				.filter(object -> this.isIn(object, region))
				.filter(object -> this.IsVisible(object, efficiency, multPower))
				.collect(Collectors.toSet());
	}

	public abstract boolean isIn(CelestialObject object, SearchRegion region);
	public abstract boolean IsVisible(CelestialObject object, float efficiency, float multPower);
}
