package stellarapi.api.world;

import com.google.common.collect.ImmutableSet;

import stellarapi.api.celestials.CelestialCollections;
import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.view.IAtmosphereEffect;
import stellarapi.api.view.ICCoordinates;

public interface ICelestialWorld {
	public ICCoordinates getCoordinate();
	public IAtmosphereEffect getSkyEffect();

	public CelestialCollections getCollections();
	public ImmutableSet<IEffectorType> getEffectorTypeSet();
	public CelestialEffectors getCelestialEffectors(IEffectorType type);
}
