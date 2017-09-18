package stellarapi.api.atmosphere;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.World;

/**
 * Atmospheric provider for each WorldSet.
 * 
 * Also used as identifier of atmosphere settings for the local atmosphere.
 * */
public interface IAtmSetProvider {
	/**
	 * Generates actual local atmosphere for certain world.
	 * This creates atmosphere from world regardless of the settings.
	 * */
	public ILocalAtmosphere generateLocalAtmosphere(World world);

	/**
	 * Decides whether the atmosphere should be replaced
	 *  with the generated one for the specified settings.
	 * */
	public boolean replaceWithSettings(World world, Object settings);

	/**
	 * Generates certain atmosphere with settings.
	 * <code>null</code> means absence of atmosphere, i.e. vacuum.
	 * */
	public @Nullable Atmosphere generateAtmosphere(World world, Object settings);

	/**
	 * Generates blank atmosphere, which will read the data saved to world.
	 * */
	public @Nonnull Atmosphere genBlankAtmosphere(World world);

	/**
	 * Generates atmosphere settings.
	 * */
	public Object generateSettings();
}
