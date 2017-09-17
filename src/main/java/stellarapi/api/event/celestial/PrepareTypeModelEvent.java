package stellarapi.api.event.celestial;

import java.util.List;

import stellarapi.api.celestials.collection.model.IRecognizer;
import stellarapi.api.celestials.collection.model.TypeModel;

public class PrepareTypeModelEvent {
	public final TypeModel theModel;
	private List<IRecognizer> recognizers;

	public PrepareTypeModelEvent(TypeModel model) {
		this.theModel = model;
	}

	/** Registers recognizer */
	public void registerRecognizer(IRecognizer recognizer) {
		recognizers.add(recognizer);
	}
	// TODO fill in this prepare typed model event
}
