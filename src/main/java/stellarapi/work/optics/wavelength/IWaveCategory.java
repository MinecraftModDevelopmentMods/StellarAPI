package stellarapi.work.optics.wavelength;

/**
 * Wave category mainly for determination of observable objects, or checking brightness by order of magnitude.
 * A wave category is determined as follows:
 * <ol>
 * <li> On <b>every reference filters</b> in a wave category,
 * 			the brightness of the object on the filters should be <b>similar</b> by order of magnitude.</li>
 * 		<ul><li> i.e. Approximately <b>observability</b> is well defined in the category.</ul>
 * <li> There always exists considerably many objects for two wave categories,
 * 			such that they have <b>different</b> brightness on their filters by order of magnitude.
 * 		<ul><li> i.e. Approximately <b>observability</b> is not identical between categories. </ul>
 * <li> Conventionally, there is equivalent branch of astronomy or pseudo-astronomy for this category.</li>
 * </ol>
 * e.g. 'Visible', 'X-Ray' and 'Radio' are wave categories,
 *  but 'Red' is not wave category because its observability is approximately same with
 *  	observability on 'Visible' category.
 * */
public interface IWaveCategory {

	/**
	 * Name of the category.
	 * */
	public String getName();

	/**
	 * Basis reference filter on the category.
	 * */
	public IReferenceFilter getBasisFilter();

	/**
	 * Checks if certain reference filter is in the category.
	 * */
	public boolean isInCategory(IReferenceFilter filter);

}