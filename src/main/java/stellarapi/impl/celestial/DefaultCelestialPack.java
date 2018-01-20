package stellarapi.impl.celestial;

import net.minecraft.world.World;
import stellarapi.api.pack.ICelestialPack;
import stellarapi.api.pack.ICelestialScene;
import stellarapi.api.world.worldset.WorldSet;

public enum DefaultCelestialPack implements ICelestialPack {
	INSTANCE;

	@Override
	public String getPackName() {
		return "Default";
	}

	@Override
	public ICelestialScene getScene(WorldSet worldSet, World world, boolean isDefault) {
		return new DefaultCelestialScene(world);
	}
}
