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
import stellarapi.impl.celestial.CelestialQuadObject;

public class SAPICollection extends BasicCelestialCollection {
	public SAPICollection(@Nullable CelestialObject sun, @Nullable CelestialObject moon) {
		super(new ResourceLocation(SAPIReferences.MODID, "system"), EnumCollectionType.System,
				0, toSet(sun, moon));
	}

	private static Set<CelestialObject> toSet(@Nullable CelestialObject obj1, @Nullable CelestialObject obj2) {
		ImmutableSet.Builder<CelestialObject> builder = ImmutableSet.builder();
		if(obj1 != null)
			builder.add(obj1);
		if(obj2 != null)
			builder.add(obj2);
		return builder.build();
	}

	@Override
	public boolean isIn(CelestialObject object, SearchRegion region) {
		if(object instanceof CelestialQuadObject)
			return ((CelestialQuadObject)object).intersects(region);
		return false;
	}

	@Override
	public boolean IsVisible(CelestialObject object, float efficiency, float multPower) {
		return true;
	}
}
