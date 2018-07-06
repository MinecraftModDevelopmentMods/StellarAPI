package stellarapi.api.interact;

import stellarapi.api.optics.Wavelength;

/**
 * Filter interface, which changes filter quantum efficiency.
 * Applied when an item with this capability is activated.
 * */
public interface IFilter {
	public float transformQE(Wavelength wavelength, float prevEfficiency);
}
