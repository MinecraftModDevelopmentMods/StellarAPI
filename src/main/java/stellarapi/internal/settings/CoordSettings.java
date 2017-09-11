package stellarapi.internal.settings;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.coordinates.ICoordProvider;
import stellarapi.api.lib.config.DynamicConfig;
import stellarapi.internal.coordinates.CoordRegistry;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderRegistry;
import worldsets.api.worldset.WorldSet;

public class CoordSettings {
	// TODO CoordSettings fill in details

	public final transient WorldSet theWorldSet;
	private final transient IProviderRegistry<ICoordProvider> coordProvRegistry;
	private final transient Map<String, ResourceLocation> nameMap;
	private final transient String[] names;

	public CoordSettings(WorldSet worldSet) {
		this.theWorldSet = worldSet;
		this.coordProvRegistry = ProviderRegistry.findRegistry(ICoordProvider.class);
		this.nameMap = coordProvRegistry.getSlaveMap(CoordRegistry.READABLE_NAMES, HashMap.class);
		this.names = nameMap.keySet().toArray(new String[0]);

		this.coordProviderName = this.names[0];
		this.defaultSettings = new WorldCoordSettings(this);
	}

	@DynamicConfig.DynamicElementProperty(
			affected = { DynamicConfig.StringCycle.class }, id = "coordProvider")
	public String coordProviderName;

	public WorldCoordSettings defaultSettings;

	@DynamicConfig.Collection(isConfigurable = true, id = "additionals")
	public Map<String, WorldCoordSettings> additionalSettings;

	@DynamicConfig.EvaluatorID("coordProvider")
	public DynamicConfig.StringCycle getStringCycle() {
		return new DynamicConfig.StringCycle() {
			@Override
			public Class<? extends Annotation> annotationType() { return DynamicConfig.StringCycle.class; }
			@Override
			public String[] value() { return CoordSettings.this.names; }
		};
	}

	public ResourceLocation getCurrentProviderID() {
		return nameMap.get(this.coordProviderName);
	}

	public ICoordProvider getCurrentProvider() {
		return coordProvRegistry.getProvider(this.getCurrentProviderID());
	}
}