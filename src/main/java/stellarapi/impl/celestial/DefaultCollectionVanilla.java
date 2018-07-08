package stellarapi.impl.celestial;

import com.google.common.collect.ImmutableSet;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.celestials.BasicCelestialCollection;
import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.celestials.EnumCollectionType;
import stellarapi.api.observe.SearchRegion;

public class DefaultCollectionVanilla extends BasicCelestialCollection {
	public DefaultCollectionVanilla(CelestialObject sun, CelestialObject moon) {
		super(new ResourceLocation("system"), EnumCollectionType.System,
				0, ImmutableSet.of(sun, moon));
	}

	@Override
	public boolean isIn(CelestialObject object, SearchRegion region) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean IsVisible(CelestialObject object, float efficiency, float multPower) {
		return true;
	}

}
