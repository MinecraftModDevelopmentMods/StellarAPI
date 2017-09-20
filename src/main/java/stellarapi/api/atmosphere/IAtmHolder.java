package stellarapi.api.atmosphere;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;

/** Generic capability holding the atmosphere. */
public interface IAtmHolder {
	/**
	 * Gets the atmosphere this object holds.
	 * <code>null</code> means there is no atmosphere, i.e. vacuum.
	 * */
	public @Nullable Atmosphere getAtmosphere();

	/**
	 * The local atmosphere - only exists for world.
	 * <code>null</code> means it's not available, or it's not yet set up.
	 *  (absence of atmosphere does not mean it's null)
	 * */
	public @Nullable ILocalAtmosphere getLocalAtmosphere();

	/**
	 * Internal method settings the atmosphere.
	 * */
	@Deprecated
	public void setAtmosphere(@Nullable Atmosphere atmosphere);

	/**
	 * Sets the provider name. Throws exception when unavailable.
	 * Internal method.
	 * */
	@Deprecated
	public void setProviderID(ResourceLocation providerID);

	/**
	 * Internal method for re-evaluation of atmospheres.
	 * Only available for world atmosphere.
	 * @param atmSettings the settings,
	 *  <code>null</code> for loading of the atmosphere (read from save)
	 * */
	@Deprecated
	public void evaluateAtmosphere(@Nullable Object atmSettings);

	/**
	 * Gets the provider ID. Can only be null if it's never set.
	 * Throws exception when unavailable.
	 * */
	public @Nullable ResourceLocation getProviderID();

	/**
	 * Gets the set-specific provider. Can only be null if id is never set.
	 * Throws exception when unavailable.
	 * */
	public @Nullable IAtmSetProvider getSetProvider();

}