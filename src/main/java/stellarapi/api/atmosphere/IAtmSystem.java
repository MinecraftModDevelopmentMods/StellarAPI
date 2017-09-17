package stellarapi.api.atmosphere;

import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;

/**
 * WorldSet Capability of Atmospheric system.
 * This one has no need to setup.
 * */
public interface IAtmSystem {
	/**
	 * Sets the provider name.
	 * Internal method.
	 * */
	@Deprecated
	public void setProviderID(ResourceLocation setProviderID);

	/**
	 * Gets the provider ID. Can only be null if it's never set.
	 * */
	public @Nullable ResourceLocation getProviderID();


	/**
	 * Checks if there's already an atmosphere in the system.
	 * */
	public boolean hasAtmosphere(ResourceLocation atmId);

	/**
	 * Gets the atmosphere information.
	 * @return the atmosphere, or <code>null</code> if there's no atmosphere for such id
	 * */
	public @Nullable Atmosphere getAtmosphere(ResourceLocation atmId);

	/**
	 * Puts atmosphere for specified id.
	 * @throws IllegalArgumentException if atmosphere for certain ID exists.
	 * */
	public boolean putAtmosphere(ResourceLocation atmId, Atmosphere atmosphere);

	/**
	 * Gets the atmospheres.
	 * */
	public Iterable<Map.Entry<ResourceLocation, Atmosphere>> getAtmospheres();

}