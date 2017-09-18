package stellarapi.api.atmosphere;

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

	/** Gets the set-specific provider. Can only be null if id is never set. */
	public @Nullable IAtmSetProvider getSetProvider();
}