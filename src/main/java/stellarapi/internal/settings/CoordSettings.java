package stellarapi.internal.settings;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.coordinates.ICoordHandler;
import stellarapi.api.coordinates.ICoordProvider;
import stellarapi.api.lib.config.DynamicConfig;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderRegistry;
import worldsets.api.worldset.WorldSet;

public class CoordSettings {
	// TODO CoordSettings fill in details

	public final transient WorldSet theWorldSet;
	private final transient IProviderRegistry<ICoordProvider> coordProvRegistry;
	private final transient ImmutableMap<String, ResourceLocation> nameToIDMap;
	private final transient String[] names;

	public CoordSettings(WorldSet worldSet) {
		this.theWorldSet = worldSet;
		this.coordProvRegistry = ProviderRegistry.findRegistry(ICoordProvider.class);

		ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
		for(ICoordProvider key : coordProvRegistry.providers())
			if(key.appliedWorldSets().contains(this.theWorldSet))
				builder.put(key.getReadableName(), key.getRegistryName());

		this.nameToIDMap = builder.build();
		this.names = nameToIDMap.keySet().toArray(new String[0]);

		this.coordProviderName = this.names[0];
		this.defaultSettings = new WorldCoordSettings(this);
	}

	@DynamicConfig.DynamicElementProperty(
			affected = { DynamicConfig.StringCycle.class })
	public String coordProviderName;

	public WorldCoordSettings defaultSettings;

	@DynamicConfig.Collection(isConfigurable = true, id = "additionals")
	public Map<String, WorldCoordSettings> additionalSettings;

	public DynamicConfig.StringCycle getStringCycle() {
		return new DynamicConfig.StringCycle() {
			@Override
			public Class<? extends Annotation> annotationType() { return DynamicConfig.StringCycle.class; }
			@Override
			public String[] value() { return CoordSettings.this.names; }
		};
	}

	public ICoordProvider getCurrentProvider() {
		return coordProvRegistry.getProvider(nameToIDMap.get(this.coordProviderName));
	}
}