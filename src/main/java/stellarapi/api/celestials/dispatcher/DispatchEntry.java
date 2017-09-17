package stellarapi.api.celestials.dispatcher;

import javax.annotation.Nullable;

import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.celestials.CelestialType;
import stellarapi.api.celestials.collection.ISegment;
import stellarapi.api.celestials.collection.model.TypeModel;

public class DispatchEntry {
	private CelestialType type;
	private ISegment currentSegment;

	/** Current type model, can be null if the segment is only processed*/
	private @Nullable TypeModel currentModel;

	/** Current celestial object, can be null when there's no associated object built */
	private @Nullable CelestialObject currentObject;

	void setTypeSegment(CelestialType type, ISegment currentSegment) {
		this.type = type;
		this.currentSegment = currentSegment;
		this.currentModel = null;
		this.currentObject = null;
	}

	void setModel(TypeModel model) {
		this.currentModel = model;
	}

	void setObject(CelestialObject object) {
		this.currentObject = object;
	}
}