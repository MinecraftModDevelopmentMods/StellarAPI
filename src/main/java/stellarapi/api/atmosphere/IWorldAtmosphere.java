package stellarapi.api.atmosphere;

/**
 * World Capabilities.
 * Stores local atmosphere
 * */
public interface IWorldAtmosphere {
	/**
	 * Sets up this atmosphere.
	 * */
	public void setupComplete();

	/**
	 * Gets the local atmosphere.
	 * */
	public ILocalAtmosphere getAtmosphere();
}
