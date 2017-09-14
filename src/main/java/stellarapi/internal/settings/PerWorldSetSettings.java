package stellarapi.internal.settings;

import worldsets.api.worldset.WorldSet;

public class PerWorldSetSettings {
	// TODO Settings fill in details

	public CelestialSettings celestials;
	public CoordSettings coordinates;
	public AtmSettings atmosphere;

	public PerWorldSetSettings(WorldSet worldSet) {
		this.celestials = new CelestialSettings(worldSet);
		this.coordinates = new CoordSettings(worldSet);
		this.atmosphere = new AtmSettings(worldSet);
	}
}