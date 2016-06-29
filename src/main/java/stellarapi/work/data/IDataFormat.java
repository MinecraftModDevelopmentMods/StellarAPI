package stellarapi.work.data;

import stellarapi.work.identify.ICelestialIdentifier;

/**
 * Data format to be processed.
 * */
public interface IDataFormat {
	/**
	 * Observability factor for this format.
	 * */
	public ICelestialIdentifier getObservabilityFactor();
}