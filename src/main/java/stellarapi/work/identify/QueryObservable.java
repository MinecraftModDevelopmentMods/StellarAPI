package stellarapi.work.identify;

import stellarapi.work.celestial.CelestialUnit;
import stellarapi.work.optics.position.ISpRegion;
import stellarapi.work.optics.wavelength.IWaveCategory;

public class QueryObservable {

	/**
	 * Celestial Unit which is observed.
	 * */
	private CelestialUnit view;

	/**
	 * Spherical Region to search for observable.
	 * */
	private ISpRegion region;
	
	private ICelestialIdentifier identifier;

	/**
	 * Minimum brightness(relative flux) allowed.<p>
	 * 1.0 for 0.0 magnitude on the basis reference filter for the category.
	 * */
	//private float minimumBrightness;

	/**
	 * The wave category to be searched for observable objects.
	 * i.e. The resulted observable objects will be observable on the wave category.
	 * */
	//private IWaveCategory category;

}