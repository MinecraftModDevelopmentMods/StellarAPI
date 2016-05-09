package stellarapi.api.optics;

import java.awt.Color;

import com.google.common.collect.ImmutableList;

/**
 * Optical filter for the entity.
 * To make an item work like filter,
 * see {@link stellarapi.example.item.ItemFilteredTelescopeExample ItemFilteredTelescopeExample}.
 * */
public interface IOpticalFilter {

	/**
	 * Filter Efficiency for the specific wavelength. <p>
	 * (1.0 means that the filter is transparent,
	 * and 0.0 means that the filter is opaque.
	 * This is specific to the wavelength)
	 * @param wavelength the wavelength to examine
	 * */
	public double getFilterEfficiency(Wavelength wavelength);

}
