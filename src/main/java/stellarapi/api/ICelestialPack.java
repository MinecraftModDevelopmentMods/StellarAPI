package stellarapi.api;

import net.minecraft.world.World;
import stellarapi.api.world.worldset.WorldSet;

/**
 * Celestial pack which can be provided by mods.
 * Singleton for each pack.
 * */
public interface ICelestialPack {
	/**
	 * Gets the pack name.
	 * */
	public String getPackName();

	/**
	 * Gets the world-specific scene.
	 * */
	public ICelestialScene getScene(WorldSet worldSet, World world, boolean vanillaServer);
}
