package worldsets.api;

import com.google.common.collect.ImmutableList;

import net.minecraft.world.World;
import worldsets.api.worldset.WorldSet;
import worldsets.api.worldset.WorldSetInstance;

@Deprecated
public interface IReference {
	public World getDefaultWorld();

	public WorldSetInstance getWorldInstance(World base, WorldSet worldSet);

	public WorldSet getPrimaryWorldSet(World world);
	public ImmutableList<WorldSet> appliedWorldSets(World world);
}