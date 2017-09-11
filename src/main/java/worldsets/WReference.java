package worldsets;

import com.google.common.collect.ImmutableList;

import net.minecraft.world.World;
import worldsets.api.IReference;
import worldsets.api.worldset.WorldSet;
import worldsets.api.worldset.WorldSetInstance;

public class WReference implements IReference {

	@Override
	public World getDefaultWorld() {
		// TODO proxy
		return null;
	}

	@Override
	public WorldSetInstance getWorldInstance(World base, WorldSet worldSet) {
		GlobalWorldSetData data = GlobalWorldSetData.getWorldSets(base);
		data.setupIfNeeded(base);
		return data.getInstance(worldSet);
	}

	@Override
	public WorldSet getPrimaryWorldSet(World world) {
		return WorldSetData.getWorldSets(world).primaryWorldSet;
	}

	@Override
	public ImmutableList<WorldSet> appliedWorldSets(World world) {
		return WorldSetData.getWorldSets(world).appliedWorldSets;
	}

}