package worldsets.api;

import com.google.common.collect.ImmutableList;

import net.minecraft.world.World;
import worldsets.api.worldset.WorldSet;

@Deprecated
public interface IReference {
	public World getDefaultWorld(boolean isRemote);

	public WorldSet getPrimaryWorldSet(World world);
	public ImmutableList<WorldSet> appliedWorldSets(World world);
}