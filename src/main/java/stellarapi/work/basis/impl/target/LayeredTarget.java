package stellarapi.work.basis.impl.target;

import com.google.common.collect.Ordering;

import stellarapi.work.basis.accuracy.IAccuracyStage;
import stellarapi.work.basis.compound.ICompound;
import stellarapi.work.basis.compound.IModifiableCompound;
import stellarapi.work.basis.target.ITarget;

public abstract class LayeredTarget<S extends IAccuracyStage> extends AbstractTarget<S> {
	private ITargetLayer<S, LayeredTarget>[] layers;

	@Override
	public void process(S stage, ICompound inspect, IModifiableCompound additional) {
		if(this.checkProcessNotRequired(stage, inspect, additional))
			return;

		this.transition(stageOrdering.compare(stage, this.getCurrentStage()), stage, inspect, additional);
	}

	/**
	 * Take a transition from a stage to another. Can be split with several stages.
	 * */
	protected void transition(int comparison, S stage, ICompound inspect, IModifiableCompound additional) {
		this.stageTransition(comparison, stage, inspect, additional);
	}

	protected void stageTransition(int comparison, S stage, ICompound inspect, IModifiableCompound additional) {
		S current = this.getCurrentStage();
		this.preProcess(comparison, current, stage, inspect, additional);
		if(comparison > 0)
			for(ITargetLayer<S, LayeredTarget> layer : this.layers)
				layer.populate(this, current, stage, inspect, additional);
		else if(comparison < 0)
			for(ITargetLayer<S, LayeredTarget> layer : this.layers)
				layer.demolish(this, current, stage, inspect, additional);
		this.postProcess(comparison, current, stage, inspect, additional);
		this.syncStageTime(stage);
	}


	protected abstract void preProcess(int comparison, S pre, S stage, ICompound inspect, IModifiableCompound additional);
	protected abstract void postProcess(int comparison, S pre, S stage, ICompound inspect, IModifiableCompound additional);

	protected abstract long captureCurrentTime();

	protected void setupLayers(ITargetLayer[] layers) {
		this.layers = layers;
	}
}
