package stellarapi.work.identify;

/**
 * Celestial Unit which can be identified as 'same object' in some case.
 * This will include galaxy supercluster, galaxy cluster, galaxy, star system, and planetary subsystems.
 * Each object should be in a unique celestial unit.
 * Objects in a celestial unit identifies any other celestial unit as one object,
 * 		if they are not ancestor/descendant of each other.
 * TODO approximation strategies
 * */
public class CelestialUnit {

	/**
	 * The parent unit for this unit.
	 * */
	private CelestialUnit parent;

	/**
	 * Approximation of its scale, in AU.
	 * */
	private double scale;

}