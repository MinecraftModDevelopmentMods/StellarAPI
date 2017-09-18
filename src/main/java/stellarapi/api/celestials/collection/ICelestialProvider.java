package stellarapi.api.celestials.collection;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.celestials.CelestialType;
import worldsets.api.provider.IProvider;
import worldsets.api.worldset.WorldSet;

public interface ICelestialProvider extends IProvider {

	/**
	 * Gets the readable name for this provider.
	 * Should be unique and should persist over time.
	 * */
	public String getReadableName();

	/**
	 * Types this provider supports.
	 * */
	public ImmutableSet<CelestialType> supportingTypes();

	/**
	 * WorldSets which can be applied wit 
	 * */
	public ImmutableSet<WorldSet> appliedWorldSets(CelestialType type);

	/**
	 * Generates the collection for the specified type and worldset.
	 * */
	public CelestialCollection generateCollection(CelestialType type, WorldSet worldSet);

	/**
	 * Generates the collection settings for the specified type and worldset.
	 * */
	public Object generateSettings(CelestialType type, WorldSet worldSet);

	/**
	 * Dependency on parent provider for the collection on specified type and worldset.
	 * TODO isn't this too simple? Predicate on collection.
	 * */
	public Predicate<ResourceLocation> parentDependency(CelestialType type, WorldSet worldSet);
}
