package worldsets.api;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import worldsets.api.worldset.WorldSet;
import worldsets.api.worldset.WorldSetFactory;

@Deprecated
public interface IReference {
	public World getDefaultWorld(boolean isRemote);

	public void registerFactory(WorldSetFactory factory);

	public ImmutableList<WorldSet> getAllWorldSets();
	public WorldSet[] getGeneratedWorldSets(ResourceLocation location);
	public WorldSet getPrimaryWorldSet(World world);
	public ImmutableList<WorldSet> appliedWorldSets(World world);
}