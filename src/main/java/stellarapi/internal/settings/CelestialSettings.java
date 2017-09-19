package stellarapi.internal.settings;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IRegistryDelegate;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.celestials.CelestialType;
import stellarapi.api.lib.config.DynamicConfig;
import worldsets.api.worldset.WorldSet;

public class CelestialSettings {

	@DynamicConfig.Collection
	public Map<IRegistryDelegate<CelestialType>, CelestialTypeSettings> celestialMap;

	CelestialSettings(WorldSet worldSet) {
		ImmutableMap.Builder<IRegistryDelegate<CelestialType>, CelestialTypeSettings> mapBuilder = ImmutableMap.builder();
		IForgeRegistry<CelestialType> typeRegistry = SAPIRegistries.getCelestialTypeRegistry();
		for(CelestialType celestialType : SAPIRegistries.getOrderedTypes())
			mapBuilder.put(celestialType.delegate, new CelestialTypeSettings(this, worldSet, celestialType));
		this.celestialMap = mapBuilder.build();
	}
}
