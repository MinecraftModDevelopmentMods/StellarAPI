package stellarapi.api.atmosphere;

/**
 * World Capability of local atmosphere.
 * Stores local atmosphere.
 * */
public interface IWorldAtmosphere {
	/**
	 * Gets the local atmosphere.
	 * */
	public ILocalAtmosphere getLocalAtmosphere();

	/**
	 * Gets the atmosphere for this world.
	 * */
	public Atmosphere getAtmosphere();
}