package stellarapi.api.optics;

import java.awt.Color;

import com.google.common.collect.ImmutableList;

/**
 * Implementation for RGB filter.
 * */
public abstract class RGBFilter implements IOpticalFilter {
	
	private final ImmutableList<WaveFilter> filterList = ImmutableList.of(
			new WaveFilter(EnumRGBA.Red),
			new WaveFilter(EnumRGBA.Green),
			new WaveFilter(EnumRGBA.Blue));

	@Override
	public boolean isRGB() {
		return true;
	}

	@Override
	public ImmutableList<? extends IWaveFilter> getFilterList() {
		return this.filterList;
	}
	
	/**
	 * gets efficiency for certain color.
	 * @param color the color in R, G or B. Alpha will not called here.
	 * */
	public abstract double getFilterEfficiency(EnumRGBA color);
	
	
	private class WaveFilter implements IWaveFilter {
		
		private EnumRGBA color;
		
		public WaveFilter(EnumRGBA color) {
			this.color = color;
		}

		@Override
		public Wavelength getWavelength() {
			return Wavelength.colorWaveMap.get(color);
		}

		@Override
		public double getFilterEfficiency() {
			return RGBFilter.this.getFilterEfficiency(color);
		}

		@Override
		public Color getColor() {
			return color.getColor();
		}
		
	}

}
