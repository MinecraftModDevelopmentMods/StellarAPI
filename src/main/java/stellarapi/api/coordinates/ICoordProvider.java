package stellarapi.api.coordinates;

import com.google.common.collect.ImmutableSet;

import worldsets.api.provider.IProvider;
import worldsets.api.worldset.WorldSet;

public interface ICoordProvider extends IProvider {

	/**
	 * Gets the readable name for this provider.
	 * Should be unique and should persist over time.
	 * */
	public String getReadableName();


	/**
	 * Gets the set of applied world sets.
	 * */
	public ImmutableSet<WorldSet> appliedWorldSets();

	/**
	 * Provide coordinates handlers for certain world set.
	 * WorldSets should be recognized first and appropriate handlers should be given.
	 * */
	public ICoordHandler generateHandler(WorldSet worldSet);

	/**
	 * Generates the coordinates settings for this handler.
	 * */
	public ICoordMainSettings generateSettings(WorldSet worldSet);
}