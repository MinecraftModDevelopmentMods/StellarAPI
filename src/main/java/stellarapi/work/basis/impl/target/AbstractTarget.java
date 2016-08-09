package stellarapi.work.basis.impl.target;

import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.collect.Ordering;

import stellarapi.work.basis.accuracy.IAccuracyStage;
import stellarapi.work.basis.compound.ICompound;
import stellarapi.work.basis.compound.IModifiableCompound;
import stellarapi.work.basis.target.ITarget;

public abstract class AbstractTarget<S extends IAccuracyStage> implements ITarget<S> {

	/** The ordering to use for stages. This should be used to avoid NPE. */
	protected final Ordering<S> stageOrdering = IAccuracyStage.getOrdering();

	private @Nullable S currentStage = null;
	private long timeChanged = this.captureCurrentTime();

	@Override
	public S getCurrentStage() {
		return this.currentStage;
	}

	@Override
	public long timeChanged() {
		return this.timeChanged;
	}

	protected boolean checkProcessNotRequired(@Nullable S stage, ICompound inspect, IModifiableCompound additional) {
		return this.timeChanged == this.captureCurrentTime() || Objects.equal(stage, this.currentStage);
	}

	protected void syncStageTime(@Nullable S stage) {
		this.currentStage = stage;
		this.timeChanged = this.captureCurrentTime();
	}

	protected abstract long captureCurrentTime();
}
