package stellarapi.api;

import com.google.common.collect.ImmutableSet;

import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.IEffectorType;

public interface ICelestialWorld {
	public ICelestialCoordinates getCoordinate();

	public ISkyEffect getSkyEffect();

	public ImmutableSet<IEffectorType> getEffectorTypeSet();

	public CelestialEffectors getCelestialEffectors(IEffectorType type);
}
