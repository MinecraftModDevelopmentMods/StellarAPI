package stellarapi.work.impl.target.layered;

import stellarapi.work.basis.accuracy.IAccuracyStage;
import stellarapi.work.basis.compound.ICompound;
import stellarapi.work.basis.compound.IModifiableCompound;

public abstract class ProgressiveTarget<S extends IAccuracyStage> extends LayeredTarget<S> {
	
	/**
	 * Take a transition from a stage to another. Can be split with several stages.
	 * */
	@Override
	protected void transition(int comparison, S stage, ICompound inspect, IModifiableCompound additional) {
		if(comparison < 0) {
			
		}
		this.stageTransition(comparison, stage, inspect, additional);
	}
	
	protected abstract S nextStage(S stage);
	protected abstract S prevStage(S stage);

}
