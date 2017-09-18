package stellarapi.internal.settings;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.atmosphere.IAtmProvider;
import stellarapi.api.coordinates.ICoordProvider;
import stellarapi.api.lib.config.DynamicConfig;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderRegistry;
import worldsets.api.worldset.WorldSet;

public class AtmSettings {
	public final transient WorldSet theWorldSet;
	private final transient IProviderRegistry<IAtmProvider> atmProvRegistry;
	private final transient BiMap<String, ResourceLocation> nameMap;
	private final transient String[] names;

	public AtmSettings(final WorldSet worldSet) {
		this.theWorldSet = worldSet;
		this.atmProvRegistry = ProviderRegistry.findRegistry(IAtmProvider.class);
		this.nameMap = atmProvRegistry.getSlaveMap(SAPIRegistries.READABLE_NAMES, HashBiMap.class);
		this.names = Iterables.toArray(Iterables.transform(
				Iterables.filter(atmProvRegistry.keys(), new Predicate<ResourceLocation>() {
					@Override
					public boolean apply(ResourceLocation input) {
						IAtmProvider provider = atmProvRegistry.getProvider(input);
						return provider.appliedWorldSets().contains(worldSet);
					}
				} ), Functions.forMap(nameMap.inverse())), String.class);

		this.atmProviderName = this.names[0];
		this.defaultSettings = new AtmWorldSettings(this);
	}

	@DynamicConfig.DynamicElementProperty(
			affected = { DynamicConfig.StringCycle.class }, id = "atmProvider")
	public String atmProviderName;

	public AtmWorldSettings defaultSettings;

	@DynamicConfig.Collection(isConfigurable = true, id = "additionals")
	public Map<String, AtmWorldSettings> additionalSettings;

	@DynamicConfig.EvaluatorID("atmProvider")
	public DynamicConfig.StringCycle getStringCycle() {
		return new DynamicConfig.StringCycle() {
			@Override
			public Class<? extends Annotation> annotationType() { return DynamicConfig.StringCycle.class; }
			@Override
			public String[] value() { return AtmSettings.this.names; }
		};
	}

	public ResourceLocation getCurrentProviderID() {
		return nameMap.get(this.atmProviderName);
	}

	public IAtmProvider getCurrentProvider() {
		return atmProvRegistry.getProvider(this.getCurrentProviderID());
	}
}
