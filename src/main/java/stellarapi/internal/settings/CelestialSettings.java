package stellarapi.internal.settings;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryDelegate;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.celestials.CelestialType;
import stellarapi.api.lib.config.DynamicConfig;
import worldsets.api.worldset.WorldSet;

public class CelestialSettings {

	@DynamicConfig.Collection
	public Map<RegistryDelegate<CelestialType>, CelestialTypeSettings> celestialMap;

	CelestialSettings(WorldSet worldSet) {
		ImmutableMap.Builder<RegistryDelegate<CelestialType>, CelestialTypeSettings> mapBuilder = ImmutableMap.builder();
		IForgeRegistry<CelestialType> typeRegistry = SAPIRegistries.getCelestialTypeRegistry();
		for(CelestialType celestialType : SAPIRegistries.getOrderedTypes())
			mapBuilder.put(celestialType.delegate, new CelestialTypeSettings(this, worldSet, celestialType));
		this.celestialMap = mapBuilder.build();
	}
}
