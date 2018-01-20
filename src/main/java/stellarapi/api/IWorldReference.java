package stellarapi.api;

import com.google.common.collect.ImmutableSet;

import stellarapi.api.celestials.CelestialCollectionManager;
import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.ICelestialCoordinates;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.world.ICelestialWorld;

/** Interface of per world reference to improve independence of api. */
public interface IWorldReference extends ICelestialWorld {
	public CelestialCollectionManager getCollectionManager();
}
