package stellarapi.example.world;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import stellarapi.api.world.ICelestialHelper;
import stellarapi.api.world.IWorldProviderReplacer;

public enum WorldReplacerDefault implements IWorldProviderReplacer {
	INSTANCE;

	@Override
	public boolean accept(World world, WorldProvider provider) {
		return true;
	}

	@Override
	public WorldProvider createWorldProvider(World world, WorldProvider originalProvider, ICelestialHelper helper) {
		return new WorldProviderDefault(world, originalProvider, helper);
	}
}
