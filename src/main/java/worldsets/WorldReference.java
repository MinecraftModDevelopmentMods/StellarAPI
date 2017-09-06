package worldsets;

import com.google.common.collect.ImmutableList;

import net.minecraft.world.World;
import worldsets.api.worldset.IWorldReference;
import worldsets.api.worldset.WorldSet;

public class WorldReference implements IWorldReference {

	@Override
	public WorldSet getPrimaryWorldSet(World world) {
		return WorldSetData.getWorldSets(world).primaryWorldSet;
	}

	@Override
	public ImmutableList<WorldSet> appliedWorldSets(World world) {
		return WorldSetData.getWorldSets(world).appliedWorldSets;
	}

}