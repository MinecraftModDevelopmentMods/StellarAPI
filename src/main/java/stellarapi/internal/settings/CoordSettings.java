package stellarapi.internal.settings;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.sun.prism.image.Coords;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.coordinates.CCoordinates;
import stellarapi.api.coordinates.ICoordProvider;
import stellarapi.api.lib.config.DynamicConfig;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderRegistry;
import worldsets.api.worldset.WorldSet;

public class CoordSettings {
	// TODO CoordSettings fill in details

	public final transient WorldSet theWorldSet;
	private final transient IProviderRegistry<ICoordProvider> coordProvRegistry;
	private final transient BiMap<String, ResourceLocation> nameMap;
	private final transient String[] names;

	public CoordSettings(final WorldSet worldSet) {
		this.theWorldSet = worldSet;
		this.coordProvRegistry = ProviderRegistry.findRegistry(ICoordProvider.class);
		this.nameMap = coordProvRegistry.getSlaveMap(SAPIRegistries.READABLE_NAMES, HashBiMap.class);
		this.names = Iterables.toArray(Iterables.transform(
				Iterables.filter(coordProvRegistry.keys(), new Predicate<ResourceLocation>() {
					@Override
					public boolean apply(ResourceLocation input) {
						ICoordProvider provider = coordProvRegistry.getProvider(input);
						if(!provider.appliedWorldSets().contains(worldSet))
							return false;
						for(CCoordinates coords : SAPIRegistries.getCoordRegistry())
							if(coords.getDefaultParentID() == null && !provider.overrideSettings(worldSet, coords))
								return false;
						return true;
					}
				}), Functions.forMap(nameMap.inverse())), String.class);

		this.coordProviderName = this.names[0];
		this.defaultSettings = new CoordWorldSettings(this);
	}

	@DynamicConfig.DynamicElementProperty(
			affected = { DynamicConfig.StringCycle.class }, id = "coordProvider")
	public String coordProviderName;

	public CoordWorldSettings defaultSettings;

	@DynamicConfig.Collection(isConfigurable = true, id = "additionals")
	public Map<String, CoordWorldSettings> additionalSettings;

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