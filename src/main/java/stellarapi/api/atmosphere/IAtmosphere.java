package stellarapi.api.atmosphere;

/** Stores local atmosphere */
public interface IAtmosphere {
	/**
	 * Sets up this atmosphere.
	 * */
	public void setupComplete();

	/**
	 * Gets the local atmosphere.
	 * */
	public ILocalAtmosphere getAtmosphere();
}
