package stellarapi.work.optics.wavelength;

/**
 * Reference Filter to be used to estimate the brightness from objects.
 * A reference filter should suffice these conditions:
 * <ol>
 * <li> Brightness on the filter should not be different from nearby filters by order of magnitude.
 * <li> Considerably many objects has brightness data on the filter.
 * </ol>
 * e.g. H-alpha and other absorption/emission filters are not reference filters,
 * 		because there are objects which have notably small brightness on them compared to nearby filters. <p>
 * Interpolation method should be provided from the object when the data does not exist on the object.
 * */
public interface IReferenceFilter {

	/**
	 * Flux in as Jy.
	 * */
	public double referenceFlux();
	
	/**
	 * Central wavelength of the filter.
	 * */
	public double getCentralWavelength();
	
	/**
	 * Full Width Half Maximum of the filter.
	 * */
	public double filterFWHM();

}
