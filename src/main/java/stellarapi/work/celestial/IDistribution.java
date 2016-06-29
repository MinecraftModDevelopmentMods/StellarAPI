package stellarapi.work.celestial;

import stellarapi.work.identify.ICelestialIdentifier;

/**
 * Distribution of objects, ignoring exceptions.
 * */
public interface IDistribution {
	public Iterable<CelestialUnit> subUnits(ICelestialIdentifier identifier, double bound);
}