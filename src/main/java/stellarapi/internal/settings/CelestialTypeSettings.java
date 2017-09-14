package stellarapi.internal.settings;

import java.lang.annotation.Annotation;
import java.util.Collections;

import javax.annotation.Nullable;

import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.celestials.CelestialType;
import stellarapi.api.celestials.collection.ICelestialProvider;
import stellarapi.api.lib.config.DynamicConfig;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderRegistry;
import worldsets.api.worldset.WorldSet;

public class CelestialTypeSettings {
	// TODO CelestialSettings fill in details

	private final transient CelestialSettings parentSettings;

	public final transient WorldSet theWorldSet;
	public final transient CelestialType theType;
	private final transient IProviderRegistry<ICelestialProvider> celProvRegistry;
	private final transient BiMap<String, ResourceLocation> nameMap;
	private transient String[] names;

	public CelestialTypeSettings(CelestialSettings parent, WorldSet worldSet, CelestialType type) {
		this.parentSettings = parent;

		this.theWorldSet = worldSet;
		this.theType = type;
		this.celProvRegistry = ProviderRegistry.findRegistry(ICelestialProvider.class);
		this.nameMap = celProvRegistry.getSlaveMap(SAPIRegistries.READABLE_NAMES, HashBiMap.class);
		this.names = this.evaluateNames();

		this.celestialProviderName = this.names[0];
	}

	private String[] evaluateNames() {
		return Iterables.toArray(
				Iterables.concat(Iterables.transform(Iterables.filter(celProvRegistry.keys(), new Predicate<ResourceLocation>() {
					@Override
					public boolean apply(ResourceLocation input) {
						ICelestialProvider provider = celProvRegistry.getProvider(input);

						if(!provider.supportingTypes().contains(CelestialTypeSettings.this.theType))
							return false;
						if(!provider.appliedWorldSets(CelestialTypeSettings.this.theType)
								.contains(CelestialTypeSettings.this.theWorldSet))
							return false;
						if(theType.getParent() != null) {
							CelestialTypeSettings parTypeSettings = parentSettings.celestialMap.get(theType.getParent().delegate);
							return provider.parentDependency(theType, theWorldSet).apply(parTypeSettings.getCurrentProviderID());
						}

						return true;
					}
				}),Functions.forMap(nameMap.inverse())), Collections.singletonList("Empty")), String.class);
	}

	@DynamicConfig.DynamicElementProperty(
			affected = { DynamicConfig.StringCycle.class }, id = "celestialProvider")
	public String celestialProviderName;

	public Object collectionSettings;

	@DynamicConfig.EvaluatorID("celestialProvider")
	public DynamicConfig.StringCycle getStringCycle() {
		this.names = this.evaluateNames();

		return new DynamicConfig.StringCycle() {
			@Override
			public Class<? extends Annotation> annotationType() { return DynamicConfig.StringCycle.class; }
			@Override
			public String[] value() { return CelestialTypeSettings.this.names; }
		};
	}

	public @Nullable ResourceLocation getCurrentProviderID() {
		return nameMap.get(this.celestialProviderName);
	}

	public @Nullable ICelestialProvider getCurrentProvider() {
		return celProvRegistry.getProvider(this.getCurrentProviderID());
	}
}