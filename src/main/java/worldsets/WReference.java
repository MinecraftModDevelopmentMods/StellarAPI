package worldsets;

import com.google.common.collect.ImmutableList;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import worldsets.api.IReference;
import worldsets.api.worldset.WorldSet;

public class WReference implements IReference {

	IForgeRegistry<WorldSet> registry;

	@Override
	public World getDefaultWorld(boolean isRemote) {
		if(isRemote)
			return WorldSetAPI.proxy.getDefaultWorld();
		else return DimensionManager.getWorld(0);
	}


	@Override
	public ImmutableList<WorldSet> getAllWorldSets() {
		return ImmutableList.copyOf(GameRegistry.findRegistry(WorldSet.class));
	}

	@Override
	public WorldSet getPrimaryWorldSet(World world) {
		return PerWorldData.getWorldSets(world).primaryWorldSet;
	}

	@Override
	public ImmutableList<WorldSet> appliedWorldSets(World world) {
		return PerWorldData.getWorldSets(world).appliedWorldSets;
	}

}