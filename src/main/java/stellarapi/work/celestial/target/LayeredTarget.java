package stellarapi.work.celestial.target;

import stellarapi.work.basis.accuracy.IAccuracyStage;
import stellarapi.work.basis.compound.ICompound;
import stellarapi.work.basis.compound.IModifiableCompound;
import stellarapi.work.basis.target.ITarget;

public abstract class LayeredTarget<S extends IAccuracyStage> implements ITarget<S> {

	private S currentStage = null;
	private long timeChanged;

	@Override
	public void process(S stage, ICompound inspect, IModifiableCompound additional) {
		boolean success = false;
		// TODO layered process

		if(success) {
			int diff = stage.compareTo(this.currentStage);
			if(diff < 0)
				this.backTransition(this.currentStage, stage);
			else if(diff > 0)
				this.populate(this.currentStage, stage);
			this.currentStage = stage;
			this.timeChanged = this.captureCurrentTime();
		}
	}

	@Override
	public S getCurrentStage() {
		return this.currentStage;
	}

	@Override
	public long timeChanged() {
		return this.timeChanged;
	}

	public abstract void populate(S previous, S next);
	public abstract void backTransition(S previous, S next);
	public abstract long captureCurrentTime();

}
