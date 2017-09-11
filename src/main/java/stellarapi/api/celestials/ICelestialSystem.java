package stellarapi.api.celestials;

import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.celestials.collection.CelestialCollection;

/**
 * Celestial system - this does not mean planetary system.
 * Bound on certain WorldSetInstance, TODO fill in here
 * */
public interface ICelestialSystem {
	public boolean isAbsent(CelestialType type);
	public @Nullable CelestialCollection getCollection(CelestialType type);
	public void rebuildCollections(Map<CelestialType, ResourceLocation> providerIDs);
}
