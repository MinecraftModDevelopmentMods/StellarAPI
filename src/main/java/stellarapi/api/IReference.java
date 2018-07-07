package stellarapi.api;

import net.minecraft.world.World;
import stellarapi.api.pack.ICelestialScene;
import stellarapi.api.world.IWorldProviderReplacer;

public interface IReference {
	public IWorldReference getPerWorldReference(World world);

	public World getDefaultWorld(boolean isRemote);

	public IWorldProviderReplacer getDefaultReplacer();

	public ICelestialScene getActivePack(World world);
}
