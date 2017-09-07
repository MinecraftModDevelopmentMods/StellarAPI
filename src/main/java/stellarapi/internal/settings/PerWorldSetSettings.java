package stellarapi.internal.settings;

import worldsets.api.worldset.WorldSet;

public class PerWorldSetSettings {
	// TODO Settings fill in details

	public CoordSettings coordinates;

	public PerWorldSetSettings(WorldSet worldSet) {
		this.coordinates = new CoordSettings(worldSet);
	}
}