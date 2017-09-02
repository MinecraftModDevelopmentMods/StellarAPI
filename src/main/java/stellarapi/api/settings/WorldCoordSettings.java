package stellarapi.api.settings;

import java.util.Map;

import stellarapi.api.coordinates.ICoordSettings;
import stellarapi.api.lib.config.DynamicConfig;

public class WorldCoordSettings {
	// TODO CoordSettings fill in details

	@DynamicConfig.Expand
	public Object mainSettings;

	@DynamicConfig.Expand
	public Map<String, ICoordSettings> specificSettings;
}