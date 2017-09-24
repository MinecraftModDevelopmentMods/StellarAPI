package stellarapi.api.instruments;

import stellarapi.api.optics.OpFilter;

/**
 * Optical filter applied on every detector.
 * */
public interface IOpticFilter {
	public OpFilter filter();
}