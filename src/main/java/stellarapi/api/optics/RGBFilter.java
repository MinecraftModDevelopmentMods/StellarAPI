package stellarapi.api.optics;

import com.google.common.collect.ImmutableMap;

public abstract class RGBFilter implements IOpticalFilter {

	private WaveExtensive extensive;

	public RGBFilter() {
		this.extensive = new WaveExtensive(ImmutableMap.of(Wavelength.red, this.getFilterEfficiency(EnumRGBA.Red),
				Wavelength.V, this.getFilterEfficiency(EnumRGBA.Green), Wavelength.B,
				this.getFilterEfficiency(EnumRGBA.Blue), Wavelength.visible, this.getFilterEfficiency(EnumRGBA.Alpha)));
	}

	@Override
	public double getFilterEfficiency(Wavelength wavelength) {
		return extensive.apply(wavelength);
	}

	public abstract double getFilterEfficiency(EnumRGBA color);

}
