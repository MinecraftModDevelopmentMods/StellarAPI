package worldsets;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;
import worldsets.api.IReference;
import worldsets.api.lib.config.IConfigHandler;
import worldsets.api.worldset.WorldSet;
import worldsets.api.worldset.WorldSetFactory;

public class WReference implements IReference, IConfigHandler {
	private Map<ResourceLocation, WorldSetFactory> factories = Maps.newHashMap();
	private List<WorldSet> worldSets = Lists.newArrayList();
	private Map<ResourceLocation, WorldSet[]> generated = Maps.newHashMap();

	@Override
	public World getDefaultWorld(boolean isRemote) {
		if(isRemote)
			return WorldSetAPI.PROXY.getDefaultWorld();
		else return DimensionManager.getWorld(0);
	}


	@Override
	public void registerFactory(WorldSetFactory factory) {
		factories.put(factory.getLocation(), factory);
	}


	@Override
	public ImmutableList<WorldSet> getAllWorldSets() {
		return ImmutableList.copyOf(this.worldSets);
	}

	@Override
	public WorldSet[] getGeneratedWorldSets(ResourceLocation location) {
		return generated.get(location);
	}

	@Override
	public WorldSet getPrimaryWorldSet(World world) {
		return PerWorldData.getWorldSets(world).appliedWorldSets.get(0);
	}

	@Override
	public ImmutableList<WorldSet> appliedWorldSets(World world) {
		return PerWorldData.getWorldSets(world).appliedWorldSets;
	}


	@Override
	public void setupConfig(Configuration config, String category) {
		config.setCategoryComment(category, "Configuration for WorldSets.");
		config.setCategoryLanguageKey(category, "config.category.worldset"); // TODO Edit language file to include this
		config.setCategoryRequiresMcRestart(category, true);

		for(WorldSetFactory factory : factories.values()) {
			String title = factory.getTitle();
			if(title != null) {
				factory.configure(config, config.getCategory(
						category + Configuration.CATEGORY_SPLITTER + title));
			}
		}
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
		for(Map.Entry<ResourceLocation, WorldSetFactory> entry : factories.entrySet()) {
			WorldSetFactory factory = entry.getValue();
			String title = factory.getTitle();
			WorldSet[] sets = factory.generate(title != null? config.getCategory(
					category + Configuration.CATEGORY_SPLITTER + title) : null);
			worldSets.addAll(Arrays.asList(sets));
			generated.put(entry.getKey(), sets);
		}
	}

	@Override
	public void saveToConfig(Configuration config, String category) {
		// Unable to save to config
	}
}