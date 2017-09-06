package worldsets.api.worldset;

import com.google.common.collect.ImmutableList;

import net.minecraft.world.World;

@Deprecated
public interface IWorldReference {

	public WorldSet getPrimaryWorldSet(World world);
	public ImmutableList<WorldSet> appliedWorldSets(World world);

}