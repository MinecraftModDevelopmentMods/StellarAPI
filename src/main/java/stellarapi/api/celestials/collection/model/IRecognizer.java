package stellarapi.api.celestials.collection.model;

import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.celestials.collection.ISegment;

public interface IRecognizer {
	/**
	 * Checks if this recognizer accepts the segment-model pair or not.
	 * */
	public boolean accept(ISegment segment, TypeModel model);

	/**
	 * Creates the celestial object for this segment and model.
	 * */
	public CelestialObject createObject(ISegment segment, TypeModel model);
}