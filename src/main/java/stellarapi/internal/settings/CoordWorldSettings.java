package stellarapi.internal.settings;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryDelegate;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.coordinates.CCoordinates;
import stellarapi.api.coordinates.ICoordProvider;
import stellarapi.api.coordinates.ICoordSettings;
import stellarapi.api.lib.config.DynamicConfig;
import worldsets.api.worldset.WorldSet;

public class CoordWorldSettings {

	private final transient CoordSettings parentSettings;
	private final transient IForgeRegistry<CCoordinates> registry;

	private transient ICoordProvider provider;
	private transient boolean mainSettingsApplied = false;
	private transient boolean spSettingsApplied = false;

	@DynamicConfig.Expand
	@DynamicConfig.Dependence(id = "mainSettings")
	public Object mainSettings;

	@DynamicConfig.Expand
	@DynamicConfig.Dependence(id = "spSettings")
	@DynamicConfig.Collection
	// TODO DynamicConfig handle map with key as registry delegate
	public Map<RegistryDelegate<CCoordinates>, ICoordSettings> specificSettings;

	public CoordWorldSettings(CoordSettings parentSettings) {
		this.parentSettings = parentSettings;
		this.registry = SAPIRegistries.getCoordRegistry();
		this.provider = parentSettings.getCurrentProvider();

		this.mainSettings = this.getMainSettings(null);
		this.specificSettings = (Map)this.getSpSettings(null);
	}

	public void checkNupdateProvider() {
		if(provider.getReadableName().equals(parentSettings.coordProviderName))
			return;

		this.provider = parentSettings.getCurrentProvider();
		this.mainSettingsApplied = false;
		this.spSettingsApplied = false;
	}

	@DynamicConfig.EvaluatorID("mainSettings")
	public Object getMainSettings(Object previous) {
		this.checkNupdateProvider();

		if(!this.mainSettingsApplied) {
			this.mainSettingsApplied = true;
			return provider.generateSettings(parentSettings.theWorldSet);
		} else return previous;
	}

	@DynamicConfig.EvaluatorID("spSettings")
	public Object getSpSettings(Object previous) {
		this.checkNupdateProvider();

		if(!this.spSettingsApplied) {
			this.spSettingsApplied = true;

			ImmutableMap.Builder<RegistryDelegate<CCoordinates>, ICoordSettings> builder = ImmutableMap.builder();
			for(CCoordinates coordinates : registry.getValues()) {
				ICoordSettings settings = coordinates.generateSettings();
				if(settings != null && !provider.overrideSettings(parentSettings.theWorldSet, coordinates))
					builder.put(coordinates.delegate, settings);
			}
			return builder.build();
		} else return previous;
	}
}