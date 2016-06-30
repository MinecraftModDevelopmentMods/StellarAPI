package stellarapi.work.celestial;

import stellarapi.work.identify.ICelestialIdentifier;

/**
 * Celestial Unit which can be identified as 'same object' in some case. <p>
 * This will include galaxy cluster, galaxy,
 * 		stellar local, star system, and planetary subsystems. <p>
 * Each object should be in a unique celestial unit. <p>
 * Objects in a celestial unit identifies any other celestial unit as one object,
 * 		if they are not ancestor/descendant of each other. <p>
 * TODO implementation of approximation strategies
 * */
public interface ICelestialUnit {
	public Iterable<ICelestialUnit> subUnits(ICelestialIdentifier identifier, double bound);
}