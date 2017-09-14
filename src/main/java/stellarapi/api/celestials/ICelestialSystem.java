package stellarapi.api.celestials;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.celestials.collection.CelestialCollection;

/**
 * Celestial system - this does not mean planetary system.
 * Bound on certain WorldSetInstance, TODO fill in these descriptions
 * */
public interface ICelestialSystem {
	/** Check if certain collection for the type is absent */
	public boolean isAbsent(CelestialType type);

	/** Gets the collection, null for absent */
	public @Nullable CelestialCollection getCollection(CelestialType type);

	/** Gets the provider ID. null for absent */
	public @Nullable ResourceLocation getProviderID(CelestialType type);

	/**
	 * Validates and sets collections with provider IDs.
	 * If the provider for the ID is not fit here, mark it as absent.
	 * */
	public void validateNset(CelestialType type, @Nullable ResourceLocation providerID);
}