package stellarapi.internal.settings;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.RegistryDelegate;
import stellarapi.api.lib.config.DynamicConfig;
import worldsets.api.worldset.WorldSet;

public enum MainSettings {
	INSTANCE;

	// TODO Settings fill in details

	@DynamicConfig.Collection
	public Map<RegistryDelegate<WorldSet>, PerWorldSetSettings> perWorldSetMap; // TODO ID evaluation

	MainSettings() {
		ImmutableMap.Builder<RegistryDelegate<WorldSet>, PerWorldSetSettings> mapBuilder = ImmutableMap.builder();
		for(WorldSet worldSet : GameRegistry.findRegistry(WorldSet.class))
			mapBuilder.put(worldSet.delegate, new PerWorldSetSettings(worldSet));
		this.perWorldSetMap = mapBuilder.build();
	}
}
