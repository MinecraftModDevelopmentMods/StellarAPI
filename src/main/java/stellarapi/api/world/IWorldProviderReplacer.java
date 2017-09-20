package stellarapi.api.world;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import worldsets.api.provider.IProvider;

/**
 * Interface of world provider replacer to replace WorldProvider with stellar world provider.
 * It's highly recommended to set world provider implements IStellarWorldProvider.
 * */
public interface IWorldProviderReplacer extends IProvider {
	/**
	 * Determine if this replacer will accept certain world and provider.
	 * @param world the world to check
	 * @param provider the provider to check
	 * @return <code>true</code> if this replacer will replace the provider.
	 * */
	public boolean accept(World world, WorldProvider provider);

	/**
	 * Create provider to replace original WorldProvider. <p>
	 * Should not return invalid WorldProvider when {@link #accept(World, WorldProvider)} is <code>true</code>.
	 * @param world the world to replace the provider
	 * @param originalProvider original provider to be replaced
	 * @param handler the world effect handler for the provider
	 * @return the provider which will replace original provider
	 * */
	public WorldProvider createWorldProvider(World world, WorldProvider originalProvider, IWorldEffectHandler handler);
}
