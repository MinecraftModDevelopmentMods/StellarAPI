package stellarapi.api.atmosphere;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;

/**
 * WorldSet Capability of Atmospheric system.
 * This one has no need to setup.
 * */
public interface IAtmSystem {

	/**
	 * Checks if this system has atmosphere information for specified id.
	 * */
	public boolean hasInfo(ResourceLocation atmId);

	/**
	 * Gets the atmosphere information.
	 * */
	public @Nullable AtmosphereInfo getInfo(ResourceLocation atmId);

	/**
	 * Gets the atmosphere information list.
	 * */
	public AtmosphereInfo[] getInfos();

	/**
	 * Creates atmosphere for specified type and id.
	 * Internal method.
	 * */
	@Deprecated
	public void createAtmosphere(AtmosphereType type, ResourceLocation atmId);

}
