package stellarapi.api;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.pack.ICelestialScene;
import stellarapi.api.world.IWorldProviderReplacer;
import stellarapi.api.world.worldset.WorldSet;
import stellarapi.api.world.worldset.WorldSetFactory;

public interface IReference {
	public IWorldReference getPerWorldReference(World world);

	public IEntityReference getPerEntityReference(Entity entity);

	public World getDefaultWorld(boolean isRemote);
	public IViewScope getDefaultScope();
	public IOpticalFilter getDefaultFilter();
	public IWorldProviderReplacer getDefaultReplacer();

	public ICelestialScene getActivePack(World world);
}
