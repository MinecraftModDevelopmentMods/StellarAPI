package stellarapi.api.world;

import com.google.common.collect.ImmutableSet;

import stellarapi.api.ISkyEffect;
import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.ICelestialCoordinates;
import stellarapi.api.celestials.IEffectorType;

public interface ICelestialWorld {
	public ICelestialCoordinates getCoordinate();

	public IAtmosphereEffect getSkyEffect();

	public ImmutableSet<IEffectorType> getEffectorTypeSet();

	public CelestialEffectors getCelestialEffectors(IEffectorType type);
}
