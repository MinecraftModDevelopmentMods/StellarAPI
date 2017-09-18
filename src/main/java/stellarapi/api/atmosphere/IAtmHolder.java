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
	 * Semi-internal method setting the atmosphere.
	 * Can be set by apply settings event.
	 * */
	@Deprecated
	public void setAtmosphere(Atmosphere atm);

	/**
	 * Internal method for evaluation of local atmosphere.
	 * Only available for atmosphere holder on world.
	 * */
	@Deprecated
	public void reevaluateLocalAtmosphere();
}