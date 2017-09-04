package stellarapi.internal.settings;

import java.util.Map;

import stellarapi.api.coordinates.ICoordHandler;
import stellarapi.api.lib.config.DynamicConfig;

public class CoordSettings {
	// TODO CoordSettings fill in details

	@DynamicConfig.DynamicElementProperty(
			affected = { DynamicConfig.StringCycle.class })
	public String coordHandlerName;

	public WorldCoordSettings defaultSettings;

	@DynamicConfig.Collection(isConfigurable = true, id = "additionals")
	public Map<String, WorldCoordSettings> additionalSettings;


	public transient boolean isCoordHandlerChanged;
	public transient ICoordHandler handler;
}