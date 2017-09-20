package worldsets;

import com.google.common.collect.ImmutableList;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import worldsets.api.IReference;
import worldsets.api.worldset.WorldSet;

public class WReference implements IReference {

	@Override
	public World getDefaultWorld(boolean isRemote) {
		// TODO proxy
		if(isRemote)
			return null;
		else return DimensionManager.getWorld(0);
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