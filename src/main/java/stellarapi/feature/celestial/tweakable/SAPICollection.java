package stellarapi.feature.celestial.tweakable;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

import stellarapi.api.celestials.AbstractCelestialCollection;
import stellarapi.api.celestials.EnumCollectionType;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.observe.SearchRegion;

public class SAPICollection extends AbstractCelestialCollection {
	public SAPICollection(@Nullable ICelestialObject sun, @Nullable ICelestialObject moon) {
		super("Tweakable Vanilla",
				withoutNull(ImmutableSet.of(sun, moon)), 0, EnumCollectionType.System);
	}

	private static Set<ICelestialObject> withoutNull(Set<ICelestialObject> set) {
		set.remove(null);
		return set;
	}

	@Override
	public boolean isIn(ICelestialObject object, SearchRegion region) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean IsVisible(ICelestialObject object, float efficiency, float multPower) {
		return true;
	}
}
