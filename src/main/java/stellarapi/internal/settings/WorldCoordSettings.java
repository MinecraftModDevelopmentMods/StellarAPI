package stellarapi.internal.settings;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import stellarapi.api.coordinates.CCoordinates;
import stellarapi.api.coordinates.ICoordSettings;
import stellarapi.api.lib.config.DynamicConfig;

public class WorldCoordSettings {

	private transient CoordSettings parentSettings;
	private transient IForgeRegistry<CCoordinates> registry;

	@DynamicConfig.Expand
	@DynamicConfig.Dependence(id = "mainSettings")
	public Object mainSettings;

	@DynamicConfig.Expand
	@DynamicConfig.Dependence(id = "spSettings")
	@DynamicConfig.Collection
	public Map<String, ICoordSettings> specificSettings;

	public WorldCoordSettings(CoordSettings parentSettings) {
		this.parentSettings = parentSettings;
		this.registry = GameRegistry.findRegistry(CCoordinates.class);
	}

	@DynamicConfig.EvaluatorID("mainSettings")
	public Object getMainSettings(Object previous) {
		if(parentSettings.isCoordHandlerChanged)
			return parentSettings.handler.generateSettings();
		else return previous;
	}

	@DynamicConfig.EvaluatorID("spSettings")
	public Object getSpSettings(Object previous) {
		if(parentSettings.isCoordHandlerChanged) {
			ImmutableMap.Builder<String, ICoordSettings> builder = ImmutableMap.builder();
			for(CCoordinates coordinates : registry.getValues()) {
				ICoordSettings settings = coordinates.generateSettings();
				if(settings != null && !parentSettings.handler.overrideSettings(coordinates))
					builder.put(settings.getName(), settings);
			}
			return builder.build();
		}
		else return previous;
	}

}