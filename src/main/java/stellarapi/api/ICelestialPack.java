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
	 * This should be read from configuration if it's not handling default case.
	 * @param worldSet the WorldSet
	 * @param world the world
	 * @param isDefault whether it's for default or not
	 *  - it should handle default case when it's <code>true</code>
	 * */
	public ICelestialScene getScene(WorldSet worldSet, World world, boolean isDefault);
}
