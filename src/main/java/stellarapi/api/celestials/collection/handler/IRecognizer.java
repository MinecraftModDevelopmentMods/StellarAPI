package stellarapi.api.celestials.collection.handler;

import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.celestials.collection.ISegment;
import stellarapi.api.celestials.collection.model.TypedModel;

public interface IRecognizer {
	/**
	 * Checks if this recognizer accepts the segment-model pair or not.
	 * */
	public boolean accept(ISegment segment, TypedModel model);

	/**
	 * Creates the celestial object for this segment and model.
	 * */
	public CelestialObject createObject(ISegment segment, TypedModel model);
}