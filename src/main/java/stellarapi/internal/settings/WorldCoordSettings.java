package stellarapi.internal.settings;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import stellarapi.api.coordinates.CCoordinates;
import stellarapi.api.coordinates.ICoordMainSettings;
import stellarapi.api.coordinates.ICoordProvider;
import stellarapi.api.coordinates.ICoordSettings;
import stellarapi.api.lib.config.DynamicConfig;

public class WorldCoordSettings {

	private final transient CoordSettings parentSettings;
	private final transient IForgeRegistry<CCoordinates> registry;

	private transient ICoordProvider provider;
	private transient ICoordMainSettings updatedMainSettings;
	private transient boolean mainSettingsApplied = false;
	private transient boolean spSettingsApplied = false;

	@DynamicConfig.Expand
	@DynamicConfig.Dependence(id = "mainSettings")
	public ICoordMainSettings mainSettings;

	@DynamicConfig.Expand
	@DynamicConfig.Dependence(id = "spSettings")
	@DynamicConfig.Collection
	public Map<String, ICoordSettings> specificSettings;

	public WorldCoordSettings(CoordSettings parentSettings) {
		this.parentSettings = parentSettings;
		this.registry = GameRegistry.findRegistry(CCoordinates.class);
		this.provider = parentSettings.getCurrentProvider();

		this.mainSettings = (ICoordMainSettings) this.getMainSettings(null);
		this.specificSettings = (Map)this.getSpSettings(null);
	}

	public void checkNupdateProvider() {
		if(provider.getReadableName().equals(parentSettings.coordProviderName))
			return;

		this.provider = parentSettings.getCurrentProvider();
		this.updatedMainSettings = provider.generateSettings(parentSettings.theWorldSet);
		this.mainSettingsApplied = false;
		this.spSettingsApplied = false;
	}

	@DynamicConfig.EvaluatorID("mainSettings")
	public Object getMainSettings(Object previous) {
		this.checkNupdateProvider();

		if(!this.mainSettingsApplied) {
			this.mainSettingsApplied = true;
			return this.updatedMainSettings;
		} else return previous;
	}

	@DynamicConfig.EvaluatorID("spSettings")
	public Object getSpSettings(Object previous) {
		this.checkNupdateProvider();

		if(!this.spSettingsApplied) {
			this.spSettingsApplied = true;

			ImmutableMap.Builder<String, ICoordSettings> builder = ImmutableMap.builder();
			for(CCoordinates coordinates : registry.getValues()) {
				ICoordSettings settings = coordinates.generateSettings();
				if(settings != null && !updatedMainSettings.overrideSettings(parentSettings.theWorldSet, coordinates))
					builder.put(settings.getName(), settings);
			}
			return builder.build();
		} else return previous;
	}

}