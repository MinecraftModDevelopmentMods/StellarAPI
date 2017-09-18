package worldsets;

import com.google.common.collect.ImmutableList;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import worldsets.api.IReference;
import worldsets.api.worldset.WorldSet;
import worldsets.api.worldset.WorldSetInstance;

public class WReference implements IReference {

	@Override
	public World getDefaultWorld(boolean isRemote) {
		// TODO proxy
		if(isRemote)
			return null;
		else return DimensionManager.getWorld(0);
	}

	@Override
	public WorldSetInstance getWorldInstance(World base, WorldSet worldSet) {
		GlobalData data = GlobalData.getWorldSets(base);
		data.setupIfNeeded();
		return data.getInstance(worldSet);
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