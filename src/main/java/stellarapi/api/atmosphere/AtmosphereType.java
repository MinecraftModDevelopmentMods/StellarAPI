package stellarapi.api.atmosphere;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public abstract class AtmosphereType extends IForgeRegistryEntry.Impl<AtmosphereType> {

	/**
	 * Gets atmosphere ID for certain world.
	 * */
	public abstract ResourceLocation atmosphereID(World world);

	/**
	 * Generates actual local atmosphere for certain world.
	 * This creates atmosphere from world regardless of the settings.
	 * */
	public abstract ILocalAtmosphere generateLocalAtmosphere(World world);

	/**
	 * Generates atmosphere settings.
	 * */
	public abstract Object generateSettings();

	/**
	 * Creates generic atmosphere.
	 * This should be populated later.
	 * */
	//public abstract IAtmosphere generateAtmosphere();
}