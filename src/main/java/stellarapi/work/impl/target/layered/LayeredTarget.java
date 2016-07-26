package stellarapi.work.impl.target.layered;

import stellarapi.work.basis.accuracy.IAccuracyStage;
import stellarapi.work.basis.compound.ICompound;
import stellarapi.work.basis.compound.IModifiableCompound;
import stellarapi.work.basis.target.ITarget;

public abstract class LayeredTarget<S extends IAccuracyStage> implements ITarget<S> {

	private S currentStage = null;
	private long timeChanged = this.captureCurrentTime();
	private ITargetLayer<S, LayeredTarget>[] layers;

	@Override
	public void process(S stage, ICompound inspect, IModifiableCompound additional) {
		if(this.checkProcess(stage, inspect, additional))
			return;

		this.transition(stage.compareTo(this.currentStage), stage, inspect, additional);
	}

	/**
	 * Take a transition from a stage to another. Can be split with several stages.
	 * */
	protected void transition(int comparison, S stage, ICompound inspect, IModifiableCompound additional) {
		this.stageTransition(comparison, stage, inspect, additional);
	}

	protected void stageTransition(int comparison, S stage, ICompound inspect, IModifiableCompound additional) {
		this.preProcess(comparison, this.currentStage, stage, inspect, additional);
		if(comparison > 0)
			for(ITargetLayer<S, LayeredTarget> layer : this.layers)
				layer.populate(this, this.currentStage, stage, inspect, additional);
		else if(comparison < 0)
			for(ITargetLayer<S, LayeredTarget> layer : this.layers)
				layer.demolish(this, this.currentStage, stage, inspect, additional);
		this.postProcess(comparison, this.currentStage, stage, inspect, additional);
		this.syncStage(stage);
	}

	@Override
	public S getCurrentStage() {
		return this.currentStage;
	}

	@Override
	public long timeChanged() {
		return this.timeChanged;
	}


	protected abstract void preProcess(int comparison, S pre, S stage, ICompound inspect, IModifiableCompound additional);
	protected abstract void postProcess(int comparison, S pre, S stage, ICompound inspect, IModifiableCompound additional);

	protected abstract long captureCurrentTime();

	protected void setupLayers(ITargetLayer[] layers) {
		this.layers = layers;
	}

	protected boolean checkProcess(S stage, ICompound inspect, IModifiableCompound additional) {
		return this.timeChanged == this.captureCurrentTime() || stage.equals(this.currentStage);
	}

	protected void syncStage(S stage) {
		this.currentStage = stage;
		this.timeChanged = this.captureCurrentTime();
	}
}
