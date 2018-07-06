package stellarapi.api.interact;

import com.google.common.collect.ImmutableMap;

import stellarapi.api.optics.EnumRGBA;
import stellarapi.api.optics.WaveExtensive;
import stellarapi.api.optics.Wavelength;

public abstract class RGBFilter implements IFilter {
	private WaveExtensive extensive;

	public RGBFilter() {
		ImmutableMap.Builder<Wavelength, Double> builder = ImmutableMap.builder();
		for(EnumRGBA color : EnumRGBA.values())
			builder.put(Wavelength.colorWaveMap.get(color), this.getFilterEfficiency(color));
		this.extensive = new WaveExtensive(builder.build());
	}

	@Override
	public float transformQE(Wavelength wavelength, float prevEfficiency) {
		return prevEfficiency * (float)extensive.apply(wavelength).doubleValue();
	}

	public abstract double getFilterEfficiency(EnumRGBA color);
}
