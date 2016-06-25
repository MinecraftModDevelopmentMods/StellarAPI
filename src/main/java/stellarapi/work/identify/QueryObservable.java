package stellarapi.work.identify;

import stellarapi.work.optics.position.ISpRegion;
import stellarapi.work.optics.wavelength.IWaveCategory;

/**
 * TODO Observer-dependency
 *  - Deal with Observer position?
 * */
public class QueryObservable {

	/**
	 * Spherical Region to search for observable.
	 * */
	private ISpRegion region;

	/**
	 * Minimum brightness(relative flux) allowed.<p>
	 * 1.0 for 0.0 magnitude on the basis reference filter for the category.
	 * */
	private float minimumBrightness;

	/**
	 * The wave category to be searched for observable objects.
	 * i.e. The resulted observable objects will be observable on the wave category.
	 * */
	private IWaveCategory category;

}