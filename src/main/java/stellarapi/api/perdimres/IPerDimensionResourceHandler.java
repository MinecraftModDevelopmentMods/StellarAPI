package stellarapi.api.perdimres;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Interface for per dimension resource handler to get per-dimension resource. <p>
 * Note that resource is only available on client.
 * */
public interface IPerDimensionResourceHandler {
	/**
	 * Determine if this getter will accept certain world and previous location.
	 * @param world the world to check
	 * @param previous the previous per-dimension location.
	 * @return <code>true</code> if this per-world getter will give per-dimension object
	 * */
	public boolean accept(World world, String resourceId, ResourceLocation previous);
	
	/**
	 * Get the per-dimension object.
	 * @param world the world to replace the provider
	 * @param previous the previous per-dimension location
	 * @return the per-dimension resource location for the world
	 * */
	public ResourceLocation getLocation(World world, String resourceId, ResourceLocation previous);
}
