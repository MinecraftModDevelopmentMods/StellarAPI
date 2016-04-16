package stellarapi.api.perdimension;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

/**
 * Interface of per-world getter to get certain per-dimension object. <p>
 * With this one can give a new object, or an wrapper to the previous object.
 * */
public interface IPerWorldGetter<T> {
	/**
	 * Determine if this getter will accept certain world and previous value.
	 * @param world the world to check
	 * @param previousObject the previous per-dimension object, might be <code>null</code>
	 * @return <code>true</code> if this per-world getter will give per-dimension object
	 * */
	public boolean accept(World world, T previousObject);
	
	/**
	 * Get the per-dimension object.
	 * @param world the world to replace the provider
	 * @param previousObject the previous per-dimension object, might be <code>null</code>
	 * @return the per-dimension object for the world
	 * */
	public T get(World world, T previousObject);
}
