package stellarapi.api.optics;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import stellarapi.api.lib.math.Spmath;

/**
 * Utility class for covering wavelengths using IDW method.
 * <p>
 * Note that this estimation method only applies to intensive properties, i.e.
 * which are not proportional to filter's bandwidth. Also this method has high
 * possibility to give strange value, so be careful with that part.
 */
public class WaveIntensive implements IWaveEstimation {
	private Map<Wavelength, Double> waveMap;

	private Wavelength[] waves;
	private double[] weights;

	public WaveIntensive(Map<Wavelength, Double> weights) {
		this.waveMap = weights;
		this.waves = new Wavelength[weights.size()];
		this.weights = new double[weights.size()];
		int index = 0;
		for (Map.Entry<Wavelength, Double> entry : waveMap.entrySet()) {
			this.waves[index] = entry.getKey();
			this.weights[index++] = entry.getValue();
		}
	}

	@Override
	public Double apply(Wavelength wavelength) {
		double res = 0.0;
		double weightedSum = 0.0;
		for (int i = 0; i < waves.length; i++) {
			if (waves[i].getLength() == wavelength.getLength())
				return weights[i];

			double weight = Spmath.quad(waves[i].getWidth() / (waves[i].getLength() - wavelength.getLength()));
			res += weights[i] * weight;
			weightedSum += weight;
		}

		return res / weightedSum;
	}

	@Override
	public WaveIntensive multiply(final Function<Wavelength, Double> toMultiply) {
		return new WaveIntensive(
				Maps.transformEntries(this.waveMap, new Maps.EntryTransformer<Wavelength, Double, Double>() {
					@Override
					public Double transformEntry(Wavelength key, Double value) {
						return value * toMultiply.apply(key);
					}
				}));
	}

	@Override
	public WaveIntensive divide(final Function<Wavelength, Double> toDivide) {
		return new WaveIntensive(
				Maps.transformEntries(this.waveMap, new Maps.EntryTransformer<Wavelength, Double, Double>() {
					@Override
					public Double transformEntry(Wavelength key, Double value) {
						return value / toDivide.apply(key);
					}
				}));
	}
}
