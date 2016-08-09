package stellarapi.work.basis.impl.target;

import stellarapi.work.basis.accuracy.IAccuracyStage;
import stellarapi.work.basis.compound.ICompound;
import stellarapi.work.basis.compound.IModifiableCompound;

/**
 * Simple progressive target.
 * While processing, only adjacency transition is allowed here.
 * */
public abstract class ProgressiveTarget<S extends IAccuracyStage> extends LayeredTarget<S> {

	/**
	 * Take a transition from a stage to another. Can be split with several stages.
	 * */
	@Override
	protected void transition(int comparison, S stage, ICompound inspect, IModifiableCompound additional) {
		S current = this.getCurrentStage();
		if(comparison > 0) {
			while(stageOrdering.compare(stage, current) > 0) {
				current = this.nextStage(current);
				this.stageTransition(comparison, current, inspect, additional);
			}
		} else {
			while(stageOrdering.compare(stage, current) < 0) {
				current = this.prevStage(current);
				this.stageTransition(comparison, current, inspect, additional);
			}
		}
	}

	protected abstract S nextStage(S stage);
	protected abstract S prevStage(S stage);
}
