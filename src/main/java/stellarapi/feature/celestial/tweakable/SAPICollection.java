package stellarapi.feature.celestial.tweakable;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.SAPIReferences;
import stellarapi.api.celestials.BasicCelestialCollection;
import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.celestials.EnumCollectionType;
import stellarapi.api.observe.SearchRegion;

public class SAPICollection extends BasicCelestialCollection {
	public SAPICollection(@Nullable CelestialObject sun, @Nullable CelestialObject moon) {
		super(new ResourceLocation(SAPIReferences.MODID, "system"), EnumCollectionType.System,
				0, withoutNull(ImmutableSet.of(sun, moon)));
	}

	private static Set<CelestialObject> withoutNull(Set<CelestialObject> set) {
		set.remove(null);
		return set;
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
