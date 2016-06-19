package stellarapi.api;

import com.google.common.collect.ImmutableSet;

import stellarapi.api.celestials.CelestialCollectionManager;
import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.IEffectorType;

/** Interface of per world reference to improve independence of api. */
public interface IPerWorldReference {

	public void constructCollections();

	public void resetCoordinate();

	public void resetSkyEffect();

	public ICelestialCoordinate getCoordinate();

	public ISkyEffect getSkyEffect();

	public ImmutableSet<IEffectorType> getEffectorTypeSet();

	public CelestialEffectors getCelestialEffectors(IEffectorType type);

	public CelestialCollectionManager getCollectionManager();

}
