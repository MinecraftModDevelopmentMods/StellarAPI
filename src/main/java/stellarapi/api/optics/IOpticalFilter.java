package stellarapi.api.optics;

import java.awt.Color;

import com.google.common.collect.ImmutableList;

public interface IOpticalFilter {

	/**
	 * <code>true</code> for RGB Filter, which is same as eye.
	 * */
	public boolean isRGB();
	
	/**
	 * Immutable list of filter spec for each wavelengths. <p>
	 * For RGB filters, must have order of R filter, G filter, B filter.
	 *  In those case, only the efficiency will be considered.
	 * */
	public ImmutableList<? extends IWaveFilter> getFilterList();
	
}
