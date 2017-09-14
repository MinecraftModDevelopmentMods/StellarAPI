package stellarapi.api.atmosphere;

import com.google.common.collect.ImmutableSet;

import worldsets.api.provider.IProvider;
import worldsets.api.worldset.WorldSet;

public interface IAtmProvider extends IProvider {

	/** Gets readable name for this provider */
	public String getReadableName();

	/**
	 * Gets the set of applied world sets.
	 * */
	public ImmutableSet<WorldSet> appliedWorldSets();

	/**
	 * Gets the atmosphere type for the specified world set.
	 * */
	public AtmosphereType getAtmosphereType(WorldSet worldset);
}
