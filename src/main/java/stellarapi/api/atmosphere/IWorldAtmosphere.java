package stellarapi.api.atmosphere;

/**
 * World Capability of local atmosphere.
 * Stores local atmosphere.
 * */
public interface IWorldAtmosphere {
	/**
	 * Sets up this atmosphere.
	 * */
	public void setupComplete();

	/**
	 * Sets the atmosphere type of the world set.
	 * Internal method.
	 * */
	@Deprecated
	public void setWorldAtmType(AtmosphereType atmosphereType);

	/**
	 * Gets the atmosphere type of the world set. Can only be null when it's not set.
	 * */
	public AtmosphereType getWorldAtmType();

	/**
	 * Gets the local atmosphere.
	 * */
	public ILocalAtmosphere getAtmosphere();
}