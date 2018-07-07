package stellarapi.api;

import stellarapi.api.celestials.CelestialCollectionManager;
import stellarapi.api.world.ICelestialWorld;

/** Interface of per world reference to improve independence of api. */
public interface IWorldReference extends ICelestialWorld {
	public CelestialCollectionManager getCollectionManager();
}
