package stellarapi.internal.settings;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraftforge.fml.common.registry.GameRegistry;
import stellarapi.api.lib.config.DynamicConfig;
import worldsets.api.worldset.WorldSet;

public enum MainSettings {
	INSTANCE;

	// TODO Settings fill in details

	@DynamicConfig.Collection
	public Map<WorldSet, PerWorldSetSettings> perWorldSetMap;

	MainSettings() {
		ImmutableMap.Builder<WorldSet, PerWorldSetSettings> mapBuilder = ImmutableMap.builder();
		for(WorldSet worldSet : GameRegistry.findRegistry(WorldSet.class).getValues())
			mapBuilder.put(worldSet, new PerWorldSetSettings(worldSet));
		this.perWorldSetMap = mapBuilder.build();
	}
}
