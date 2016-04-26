package stellarapi.api.optics;

import java.awt.Color;

/**
 * Filter for specific wavelength.
 * To make an item work like filtered telescope, see {@link stellarapi.example.item.ItemFilteredTelescopeExample}.
 * */
public interface IWaveFilter {
	
	/**Filtering Wavelength, should be constant.*/
	public Wavelength getWavelength();
	
	/**Filtering Efficiency (1.0 for fully transparent, 0.0 for fully opaque)*/
	public double getFilterEfficiency();

	/** Output Color. */
	public Color getColor();
}
