package stellarapi.api.coordinates;

import worldsets.api.worldset.WorldSet;

public interface ICoordMainSettings {
	/**
	 * Override settings from certain coordinates, so that it's not specified in configuration.
	 * */
	public boolean overrideSettings(WorldSet worldSet, CCoordinates coordinate);
}
