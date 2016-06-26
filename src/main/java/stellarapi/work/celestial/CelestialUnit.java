package stellarapi.work.celestial;

/**
 * Celestial Unit which can be identified as 'same object' in some case. <p>
 * This will include galaxy cluster, galaxy,
 * 		stellar local, star system, and planetary subsystems. <p>
 * Each object should be in a unique celestial unit. <p>
 * Objects in a celestial unit identifies any other celestial unit as one object,
 * 		if they are not ancestor/descendant of each other. <p>
 * TODO implementation of approximation strategies
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