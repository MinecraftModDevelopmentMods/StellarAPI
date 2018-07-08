package stellarapi.api.world.worldset;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public interface IWorldSetReference {
	public void registerFactory(WorldSetFactory factory);

	public ImmutableList<WorldSet> getAllWorldSets();
	public WorldSet[] getGeneratedWorldSets(ResourceLocation location);
	public WorldSet getPrimaryWorldSet(World world);
	public ImmutableList<WorldSet> appliedWorldSets(World world);
}
