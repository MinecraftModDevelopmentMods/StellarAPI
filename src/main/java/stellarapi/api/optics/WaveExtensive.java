package stellarapi.api.optics;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import stellarapi.api.lib.math.Spmath;

/**
 * Utility class for covering wavelengths.
 * <p>
 * This estimation method only applies to extensive properties, i.e. which are
 * proportional to filter's bandwidth.
 * <p>
 * Also this method has high possibility to give strange value, so be careful
 * with that part.
 */
public class WaveExtensive implements IWaveEstimation {
	private Map<Wavelength, Double> waveMap;

	private Wavelength[] waves;
	private double[] weights;

	public WaveExtensive(Map<Wavelength, Double> weights) {
		this.waveMap = weights;
		this.waves = new Wavelength[weights.size()];
		this.weights = new double[weights.size()];
		int index = 0;
		for (Map.Entry<Wavelength, Double> entry : waveMap.entrySet()) {
			this.waves[index] = entry.getKey();
			this.weights[index++] = entry.getValue();
		}
	}

	public Double apply(Wavelength wavelength) {
		double res = 0.0;
		double weightedSum = 0.0;
		double width = wavelength.getWidth();

		if (waveMap.containsKey(wavelength))
			return waveMap.get(wavelength);

		for (int i = 0; i < waves.length; i++) {
			if (waves[i].getLength() == wavelength.getLength())
				return weights[i] * width / waves[i].getWidth();

			double weight = Spmath.quad(waves[i].getWidth() / (waves[i].getLength() - wavelength.getLength()));
			res += weights[i] / waves[i].getWidth() * weight;
			weightedSum += weight;
		}

		return res / weightedSum * width;
	}

	@Override
	public WaveExtensive multiply(final Function<Wavelength, Double> toMultiply) {
		return new WaveExtensive(
				Maps.transformEntries(this.waveMap, new Maps.EntryTransformer<Wavelength, Double, Double>() {
					@Override
					public Double transformEntry(Wavelength key, Double value) {
						return value * toMultiply.apply(key);
					}
				}));
	}

	@Override
	public WaveExtensive divide(final Function<Wavelength, Double> toDivide) {
		return new WaveExtensive(
				Maps.transformEntries(this.waveMap, new Maps.EntryTransformer<Wavelength, Double, Double>() {
					@Override
					public Double transformEntry(Wavelength key, Double value) {
						return value / toDivide.apply(key);
					}
				}));
	}
}
