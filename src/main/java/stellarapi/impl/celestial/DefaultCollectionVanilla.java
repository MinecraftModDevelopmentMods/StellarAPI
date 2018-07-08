package stellarapi.impl.celestial;

import com.google.common.collect.ImmutableSet;

import net.minecraft.world.World;
import stellarapi.api.celestials.AbstractCelestialCollection;
import stellarapi.api.celestials.EnumCollectionType;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.observe.SearchRegion;

public class DefaultCollectionVanilla extends AbstractCelestialCollection {
	public DefaultCollectionVanilla(ICelestialObject sun, ICelestialObject moon) {
		super("Vanilla", ImmutableSet.of(sun, moon), 0, EnumCollectionType.System);
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
