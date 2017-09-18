package stellarapi.api.atmosphere;

import javax.annotation.Nullable;

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
	 * Checks if atmosphere setup is done.
	 * Internal method.
	 * */
	@Deprecated
	public boolean isAtmosphereSetup();

	/**
	 * Internal method settings the atmosphere.
	 * */
	@Deprecated
	public void setAtmosphere(@Nullable Atmosphere atmosphere);

	/**
	 * Internal method for re-evaluation of atmospheres.
	 * Only available for world atmosphere.
	 * @param atmSettings the settings,
	 *  <code>null</code> for loading of the atmosphere (read from save)
	 * */
	@Deprecated
	public void reevaluateAtmosphere(@Nullable Object atmSettings);
}